package cmf.rabbit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;
import cmf.bus.berico.IEnvelopeDispatcher;
import cmf.bus.berico.IEnvelopeReceivedCallback;
import cmf.bus.berico.ITransportProvider;
import cmf.rabbit.topology.Exchange;
import cmf.rabbit.topology.ITopologyService;
import cmf.rabbit.topology.RouteInfo;
import cmf.rabbit.topology.RoutingInfo;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class RabbitTransportProvider implements ITransportProvider {

	protected List<IEnvelopeReceivedCallback> envCallbacks;
    protected Map<IRegistration, RabbitListener> listeners;
    protected ITopologyService topoSvc;
    protected RabbitConnectionFactory connFactory;
    protected Logger log;
    
    
    public void onEnvelopeReceived(IEnvelopeReceivedCallback callback) {
    	this.envCallbacks.add(callback);
    }
    
    
    public RabbitTransportProvider(
    		ITopologyService topologyService,
    		RabbitConnectionFactory connFactory) {
    	
    	this.topoSvc = topologyService;
        this.connFactory = connFactory;
        
        this.envCallbacks = new ArrayList<IEnvelopeReceivedCallback>();
        this.listeners = new HashMap<IRegistration, RabbitListener>();
        
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    
    @Override
    @SuppressWarnings({ "deprecation", "unchecked" })
    public void send(Envelope env) throws Exception
    {
        log.debug("Enter Send");

        // first, get the topology based on the headers
        RoutingInfo routing = topoSvc.getRoutingInfo(env.getHeaders());

        // next, pull out all the producer exchanges
        List<Exchange> exchanges = new ArrayList<Exchange>();
        for (RouteInfo route : routing.getRoutes()) {
        	exchanges.add(route.getProducerExchange());
        }

        // for each exchange, send the envelope
        for (Exchange ex : exchanges)
        {
            log.debug("Sending to exchange: " + ex.toString());
            Connection conn = connFactory.connectTo(ex);
            
            Channel channel = null;
            try
            {
            	channel = conn.createChannel();
            
            	BasicProperties props = new BasicProperties.Builder().build();
            	Map<String, Object> headers = new HashMap<String, Object>();
				for (Entry<String, String> entry : env.getHeaders().entrySet()) {
				    headers.put(entry.getKey(), entry.getValue());
				}
                props.setHeaders(headers);

                channel.exchangeDeclare(ex.getName(), ex.getExchangeType(), ex.getIsDurable(), ex.getIsAutoDelete(), ex.getArguments());
                channel.basicPublish(ex.getName(), ex.getRoutingKey(), props, env.getPayload());
            }
            catch(Exception e) {
            	log.error("Failed to send an envelope", e);
            	throw e;
            }
            finally
            {
            	if (null != channel) { channel.close(); }
            }
        }

        log.debug("Leave Send");
    }
    
    @Override
    public void register(IRegistration registration) throws Exception {
        log.debug("Enter Register");

        // first, get the topology based on the registration info
        RoutingInfo routing = topoSvc.getRoutingInfo(registration.getRegistrationInfo());

        // next, pull out all the producer exchanges
        List<Exchange> exchanges = new ArrayList<Exchange>();
        for (RouteInfo route : routing.getRoutes()) {
        	exchanges.add(route.getConsumerExchange());
        }

        for (Exchange ex : exchanges) {
        	// connect to the exchange 
            Connection conn = connFactory.connectTo(ex);

            // create a channel
            Channel channel = conn.createChannel();
            
            // create a listener
            RabbitListener listener = new RabbitListener(registration, ex, channel);
            
            // hook into the listener's events
            listener.onEnvelopeReceived(new IEnvelopeReceivedCallback() {
            	@Override
				public void handleReceive(IEnvelopeDispatcher dispatcher) {
            		raise_onEnvelopeReceivedEvent(dispatcher);
            	}
            });
            listener.onClose(new IListenerCloseCallback() {
				@Override
				public void onClose(IRegistration registration) {
					listeners.remove(registration);
				}
            });

            listener.start();
            
            // store the listener
            listeners.put(registration, listener);
        }

        log.debug("Leave Register");
    }
    
    public void unregister(IRegistration registration)
    {
        if (listeners.containsKey(registration))
        {
            RabbitListener listener = listeners.get(registration);
            listener.stopListening();

            listeners.remove(registration);
        }
    }
    
    
    protected void raise_onEnvelopeReceivedEvent(IEnvelopeDispatcher dispatcher)
    {
    	for (IEnvelopeReceivedCallback callback : envCallbacks) {
    		try { callback.handleReceive(dispatcher); }
    		catch(Exception ex) { log.error("Caught an unhandled exception raising the onEnvelopeReceived event", ex); }
    	}
    }


	@Override
	public void dispose() {
		try { this.connFactory.dispose(); }
		catch (Exception ex) {}
		
		try { this.topoSvc.dispose(); }
		catch (Exception ex) {}
		
		for (RabbitListener l : this.listeners.values()) {
			try { l.dispose(); }
			catch (Exception ex) {}
		}
	}
	
	@Override
	protected void finalize() {
		this.dispose();
	}
}

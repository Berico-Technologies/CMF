package cmf.bus.berico.rabbit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;
import cmf.bus.berico.IEnvelopeReceivedCallback;
import cmf.bus.berico.ITransportProvider;
import cmf.bus.berico.rabbit.support.RabbitEnvelopeHelper;
import cmf.bus.berico.rabbit.support.RabbitRegistrationHelper;
import cmf.bus.berico.rabbit.topology.ITopologyService;
import cmf.bus.berico.rabbit.topology.RouteInfo;
import cmf.bus.berico.rabbit.topology.RoutingInfo;
import cmf.bus.berico.rabbit.topology.Exchange;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class RabbitTransportProvider implements ITransportProvider {

    protected Map<IRegistration, RabbitListener> listeners;
    protected ITopologyService topoSvc;
    protected RabbitConnectionFactory connFactory;
    protected Logger log;
    
    
    public RabbitTransportProvider(
    		ITopologyService topologyService,
    		RabbitConnectionFactory connFactory) {
    	
    	this.topoSvc = topologyService;
        this.connFactory = connFactory;
        
        this.listeners = new HashMap<IRegistration, RabbitListener>();
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    
    public void send(Envelope env)
    {
        log.Debug("Enter Send");

        // first, get the topology based on the headers
        RoutingInfo routing = topoSvc.GetRoutingInfo(env.getHeaders());

        // next, pull out all the producer exchanges
        List<Exchange> exchanges = new ArrayList<Exchange>();
        for (RouteInfo route : routing.getRoutes()) {
        	exchanges.add(route.getProducerExchange());
        }

        // for each exchange, send the envelope
        foreach (Exchange ex in exchanges)
        {
            _log.Debug("Sending to exchange: " + ex.ToString());
            IConnection conn = _connFactory.ConnectTo(ex);
            
            using (IModel channel = conn.CreateModel())
            {
                IBasicProperties props = channel.CreateBasicProperties();
                props.Headers = env.Headers as IDictionary;

                channel.ExchangeDeclare(ex.Name, ex.ExchangeType, ex.IsDurable, ex.IsAutoDelete, ex.Arguments);
                channel.BasicPublish(ex.Name, ex.RoutingKey, props, env.Payload);
            }
        }

        _log.Debug("Leave Send");
    }
//    private void createExchange(String exchangeName) {
//        try {
//            channel.exchangeDeclare(exchangeName, TOPIC_EXCHANGE_TYPE, true);
//        } catch (IOException e) {
//            throw new RuntimeException(String.format("Error declaring exchange \"%s\"", exchangeName), e);
//        }
//    }
//
//    @Override
//    protected void finalize() {
//        connectionProvider.closeConnection(connection);
//    }
//
//    private String getExchangeName(Route route) {
//        String exchangeName = route.getExchangeName();
//        if (exchangesKnownToExist.add(exchangeName)) {
//            createExchange(exchangeName);
//            exchangesKnownToExist.add(exchangeName);
//        }
//
//        return exchangeName;
//    }
//
//    @Override
//    public void register(IRegistration registration, IEnvelopeReceivedCallback callback) {
//        Channel channel = connectionProvider.newChannel(connection);
//        Queue queue = queueProvider.newQueue(channel, registration, callback);
//        queues.put(registration, queue);
//        callbacks.put(registration, callback);
//
//        String routingKey = RabbitRegistrationHelper.RegistrationInfo.getRoutingKey(registration);
//        Collection<Route> routes = topologyProvider.getReceiveRoutes(routingKey);
//        for (Route route : routes) {
//            queue.bind(getExchangeName(route), routingKey);
//        }
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public void send(Envelope envelope) {
//        String routingKey = RabbitEnvelopeHelper.Headers.getType(envelope);
//        List<Route> sendRoutes = topologyProvider.getSendRoutes(routingKey);
//        byte[] payload = envelope.getPayload();
//        Map<String, Object> headers = new HashMap<String, Object>();
//        for (Entry<String, String> entry : envelope.getHeaders().entrySet()) {
//            headers.put(entry.getKey(), entry.getValue());
//        }
//        BasicProperties basicProperties = new BasicProperties.Builder().build();
//        basicProperties.setHeaders(headers);
//        for (Route route : sendRoutes) {
//            String exchangeName = getExchangeName(route);
//            try {
//                channel.basicPublish(exchangeName, routingKey, basicProperties, payload);
//            } catch (IOException e) {
//                throw new RuntimeException("Error sending envelope", e);
//            }
//        }
//    }
//
//    @Override
//    public void unregister(IRegistration registration) {
//        Queue queue = queues.remove(registration);
//        if (queue != null) {
//            queue.stop();
//            callbacks.remove(registration);
//        }
//    }
}

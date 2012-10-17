package cmf.rabbit;

import java.util.List;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import cmf.bus.berico.EnvelopeHelper;
import cmf.bus.berico.IEnvelopeReceivedCallback;
import cmf.rabbit.topology.Exchange;

public class RabbitListener extends DefaultConsumer {
	
	protected List<IEnvelopeReceivedCallback> envCallbacks;
	protected List<IListenerCloseCallback> closeCallbacks;
	protected IRegistration registration;
    protected boolean shouldContinue;
    protected Logger log;
    protected Exchange exchange;
    protected Channel channel;
    
	
    public void onEnvelopeReceived(IEnvelopeReceivedCallback callback) {
    	envCallbacks.add(callback);
    }
    
    public void onClose(IListenerCloseCallback callback) {
    	closeCallbacks.add(callback);
    }
    

    public RabbitListener(IRegistration registration, Exchange exchange, Channel channel)
    {
    	super(channel);
    	
    	this.registration = registration;
        this.exchange = exchange;
        this.channel = channel;

        log = LoggerFactory.getLogger(this.getClass());
    }

    
    @SuppressWarnings("unchecked")
	public void initialize() throws Exception {
        channel.exchangeDeclare(exchange.getName(), exchange.getExchangeType(), exchange.getIsDurable(), exchange.getIsAutoDelete(), exchange.getArguments());
        channel.queueDeclare(exchange.getQueueName(), exchange.getIsDurable(), false, exchange.getIsAutoDelete(), exchange.getArguments());
        channel.queueBind(exchange.getQueueName(), exchange.getName(), exchange.getRoutingKey(), exchange.getArguments());
    }
    
    public void handleDelivery(
    		String consumerTag, 
    		com.rabbitmq.client.Envelope rabbitEnvelope,
            BasicProperties properties, 
            byte[] body) {

        Envelope env = new Envelope();
        new EnvelopeHelper(env).setReceiptTime(DateTime.now());
        env.setPayload(body);
        
        for (Entry<String, Object> prop : properties.getHeaders().entrySet()) {
        	
        	try {
	        	String key = prop.getKey();
	        	String value = new String((byte[])prop.getValue(), "UTF-8");
	        	
	        	env.setHeader(key, value);
        	}
        	catch (Exception ex) { log.debug("couldn't get property", ex); }
        }

        if (this.ShouldRaiseEvent(registration.getFilterPredicate(), env))
        {
            RabbitEnvelopeDispatcher dispatcher = new RabbitEnvelopeDispatcher(registration, env, channel, rabbitEnvelope.getDeliveryTag());
            this.raise_onEnvelopeReceivedEvent(dispatcher);
        }
    }
    
    
    protected boolean ShouldRaiseEvent(IEnvelopeFilterPredicate filter, Envelope env) {
    	// if there's no filter, the client wants it.  Otherwise, see if they want it.
        return (null == filter) ? true : filter.filter(env);
    }
    
    
    public void stop()
    {
        try { channel.close(); }
        catch (Exception ex) { log.debug("Caught an exception stopping the listener", ex); }
        
        this.raise_onCloseEvent(this.registration);
    }


    protected void raise_onCloseEvent(IRegistration registration)
    {
        for (IListenerCloseCallback callback : this.closeCallbacks) {
        	try { callback.onClose(registration); }
        	catch(Exception ex) { log.error("Caught an unhandled exception raising the listener close event", ex); }
        }
    }

    protected void raise_onEnvelopeReceivedEvent(RabbitEnvelopeDispatcher dispatcher)
    {
        for (IEnvelopeReceivedCallback callback : this.envCallbacks) {
        	try { callback.handleReceive(dispatcher); }
        	catch (Exception ex) { log.error("Caught an unhandled exception raising the envelope received event", ex); }
        }
    }

}

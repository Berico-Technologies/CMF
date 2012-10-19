package cmf.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;
import cmf.bus.berico.IEnvelopeDispatcher;

import com.rabbitmq.client.Channel;

public class RabbitEnvelopeDispatcher implements IEnvelopeDispatcher {

	protected Envelope env;
	protected IRegistration registration;
	protected Channel channel;
	protected long deliveryTag;
	protected Logger log;
	
	
	@Override
	public Envelope getEnvelope() { return env; }
	public void setEnvelope(Envelope env) { this.env = env; }

	public IRegistration getRegistration() { return registration; }
	public void setRegistration(IRegistration registration) { this.registration = registration; }

	public Channel getChannel() { return channel; }
	public void setChannel(Channel channel) { this.channel = channel; }

	public long getDeliveryTag() { return deliveryTag; }
	public void setDeliveryTag(long deliveryTag) { this.deliveryTag = deliveryTag; }

	
	public RabbitEnvelopeDispatcher(IRegistration registration, Envelope env,
			Channel channel, long deliveryTag) {

		this.registration = registration;
		this.env = env;
		this.channel = channel;
		this.deliveryTag = deliveryTag;
		
		this.log = LoggerFactory.getLogger(this.getClass());
	}

	
	@Override
	public void dispatch() {
        log.debug("Enter Dispatch()");
        this.dispatch(this.env);
        log.debug("Leave Dispatch()");
	}

	@Override
	public void dispatch(Envelope envelope) {
        log.debug("Enter Dispatch(env)");
        Object maybeNull = null;

        try
        {
            // this may be null, or it may be any other kind of object
            maybeNull = this.registration.handle(env);
            log.debug("Dispatched envelope to registration");
        }
        catch (Exception ex)
        {
            // no fail handler in java just yet
        	log.error("Caught an unhandled exception dispatching an envelope", ex);
            //try { registration..HandleFailed(env, ex); }
            //catch { }
        }

        try {this.respondToMessage(maybeNull); }
        catch (Exception ex) { log.error("Failed to respond to RabbitMQ", ex); }
        
        log.debug("Leave Dispatch(env)");
	}

	@Override
	public void dispatchFailed(Envelope envelope, Exception e) {
		// TODO Auto-generated method stub
		return;
	}

	
    protected void respondToMessage(Object maybeNull) throws Exception
    {
        log.debug("Enter RespondToMessage");

        // we accept an envelope instead of dispatching the envelope in our
        // state because whoever is consuming us may have mutated it
        DeliveryOutcomes result = DeliveryOutcomes.Null;

        // by convention, if handlers return nothing, assume acknowledgement
        // if the object is not null and a DeliveryOutcome, cast it
        // else, assume acknowledgement
        if (null == maybeNull) { result = DeliveryOutcomes.Acknowledge; }
        else if (maybeNull instanceof DeliveryOutcomes) { result = (DeliveryOutcomes)maybeNull; }
        else { result = DeliveryOutcomes.Acknowledge; }

        log.info("DeliveryOutcome of handled event is: " + result);
        switch (result)
        {
            case Acknowledge:
                this.channel.basicAck(this.deliveryTag, false);
                break;
            case Null:
            	this.channel.basicAck(this.deliveryTag, false);
                break;
            case Reject:
            	this.channel.basicReject(this.deliveryTag, false);
                break;
            case Exception:
            	this.channel.basicNack(this.deliveryTag, false, false);
                break;
        }

        log.debug("Leave RespondToMessage");
    }
}

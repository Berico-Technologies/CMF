package cmf.eventing.berico;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import cmf.bus.berico.EnvelopeHeaderConstants;
import cmf.eventing.IEventHandler;

public class EventRegistration implements IRegistration {

	protected IEnvelopeFilterPredicate filterPredicate;
	protected Map<String, String> registrationInfo;
	protected List<IInboundEventProcessor> inboundChain;
	protected IEventHandler eventHandler;
	
	
	@Override
	public IEnvelopeFilterPredicate getFilterPredicate() { return filterPredicate; }
	public void setFilterPredicate(IEnvelopeFilterPredicate filterPredicate) { this.filterPredicate = filterPredicate; }
	
	
	@Override
	public Map<String, String> getRegistrationInfo() { return registrationInfo; }
	public void setRegistrationInfo(Map<String, String> registrationInfo) { this.registrationInfo = registrationInfo; }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EventRegistration(IEventHandler eventHandler, List<IInboundEventProcessor> inboundChain) {
		
		this.eventHandler = eventHandler;
		this.inboundChain = inboundChain;
		
		this.registrationInfo = new HashMap<String, String>();
		this.registrationInfo.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, eventHandler.getEventType().getCanonicalName());
	}
	
	
	@Override
	public Object handle(Envelope env) throws Exception {
		Object ev = null;
		Object result = null;

		ProcessingContext processorContext = new ProcessingContext(env, ev);
        if (this.processInbound(processorContext))
        {
            try
            {
                result = eventHandler.handle(processorContext.getEvent(), processorContext.getEnvelope().getHeaders());
            }
            catch (Exception ex)
            {
                result = this.handleFailed(env, ex);
            }
        }

        return result;
	}

	@Override
	public Object handleFailed(Envelope env, Exception ex) throws Exception {
        try
        {
            return eventHandler.handleFailed(env, ex);
        }
        catch (Exception failedToFail)
        {
            throw failedToFail;
        }
	}
	
	
    protected boolean processInbound(ProcessingContext processorContext)
    {
    	boolean processed = true;

        try {
            for (IInboundEventProcessor processor : this.inboundChain) {
                if (!processor.processInbound(processorContext)) {
                    processed = false;
                    break;
                }
            }
        }
        catch (Exception ex)
        {
            processed = false;
        }

        return processed;
    }
}

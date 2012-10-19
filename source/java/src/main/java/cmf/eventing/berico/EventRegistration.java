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
import cmf.eventing.IInboundEventProcessor;

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

        if (this.processInbound(ev, env))
        {
            try
            {
                result = eventHandler.handle(ev, env.getHeaders());
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
	
	
    protected boolean processInbound(Object ev, Envelope env)
    {
    	boolean processed = true;
        Map<String, Object> processorContext = new HashMap<String, Object>();

        try {
            for (IInboundEventProcessor processor : this.inboundChain) {
                if (!processor.processInbound(ev, env, processorContext)) {
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

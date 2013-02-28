package cmf.eventing.berico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.EnvelopeHeaderConstants;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import cmf.eventing.IEventHandler;

public class EventRegistration implements IRegistration {

    @SuppressWarnings("rawtypes")
	protected IEventHandler eventHandler;
    protected IEnvelopeFilterPredicate filterPredicate;
    protected IInboundProcessorCallback processorCallback;
    protected Map<String, String> registrationInfo;


    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() {
        return filterPredicate;
    }

    @Override
    public Map<String, String> getRegistrationInfo() {
        return registrationInfo;
    }
    
    
    @SuppressWarnings({ "rawtypes" })
    public EventRegistration(IEventHandler eventHandler, IInboundProcessorCallback processorCallback) {

        this.eventHandler = eventHandler;
        this.processorCallback = processorCallback;

        registrationInfo = new HashMap<String, String>();
        registrationInfo.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, eventHandler.getEventType().getCanonicalName());
    }
    
    
	@Override
	@SuppressWarnings("unchecked")
	public Object handle(Envelope env) throws Exception {
		
		Object event = this.processorCallback.ProcessInbound(env);
		Object result = null;
		
		if (null != event) {
            try {
                result = eventHandler.handle(event, env.getHeaders());
            } catch (Exception ex) {
                result = handleFailed(env, ex);
            }
		}

        return result;
    }

    @Override
    public Object handleFailed(Envelope env, Exception ex) throws Exception {
        try {
            return eventHandler.handleFailed(env, ex);
        } catch (Exception failedToFail) {
            throw failedToFail;
        }
    }

    public void setFilterPredicate(IEnvelopeFilterPredicate filterPredicate) {
        this.filterPredicate = filterPredicate;
    }

    public void setRegistrationInfo(Map<String, String> registrationInfo) {
        this.registrationInfo = registrationInfo;
    }
}

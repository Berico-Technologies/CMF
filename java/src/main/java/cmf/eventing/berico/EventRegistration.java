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
    
    protected List<IInboundEventProcessor> inboundChain;
    
    protected Map<String, String> registrationInfo;

    @SuppressWarnings({ "rawtypes" })
    public EventRegistration(IEventHandler eventHandler, List<IInboundEventProcessor> inboundChain) {

        this.eventHandler = eventHandler;
        this.inboundChain = inboundChain;

        registrationInfo = new HashMap<String, String>();
        registrationInfo.put(EnvelopeHeaderConstants.MESSAGE_TOPIC, eventHandler.getEventType().getCanonicalName());
    }

    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() {
        return filterPredicate;
    }

    @Override
    public Map<String, String> getRegistrationInfo() {
        return registrationInfo;
    }
    
	@Override
	@SuppressWarnings("unchecked")
	public Object handle(Envelope env) throws Exception {
        Object ev = null;
        Object result = null;

        ProcessingContext processorContext = new ProcessingContext(env, ev);
        if (processInbound(processorContext)) {
            try {
                result = eventHandler.handle(processorContext.getEvent(), processorContext.getEnvelope().getHeaders());
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

    protected boolean processInbound(ProcessingContext processorContext) {
        boolean processed = true;

        try {
            for (IInboundEventProcessor processor : inboundChain) {
                if (!processor.processInbound(processorContext)) {
                    processed = false;
                    break;
                }
            }
        } catch (Exception ex) {
            processed = false;
        }

        return processed;
    }

    public void setFilterPredicate(IEnvelopeFilterPredicate filterPredicate) {
        this.filterPredicate = filterPredicate;
    }

    public void setRegistrationInfo(Map<String, String> registrationInfo) {
        this.registrationInfo = registrationInfo;
    }
}

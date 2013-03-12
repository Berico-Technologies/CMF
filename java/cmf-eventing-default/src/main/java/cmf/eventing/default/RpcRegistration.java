package cmf.eventing.default;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.time.StopWatch;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IRegistration;
import cmf.bus.berico.EnvelopeHelper;


public class RpcRegistration implements IRegistration {

    protected static final Logger log = LoggerFactory.getLogger(RpcRegistration.class);

    
    protected IInboundProcessorCallback envelopeOpener;
    protected Map<String, String> registrationInfo;
    protected Envelope responseEnvelope;
    protected Object responseEvent;
    protected IEnvelopeFilterPredicate responseFilter;

    
    public RpcRegistration(final UUID requestId, String expectedTopic, IInboundProcessorCallback envelopeOpener) {
    	
        this.envelopeOpener = envelopeOpener;

        responseFilter = new IEnvelopeFilterPredicate() {

            @Override
            public boolean filter(Envelope envelope) {
                return new EnvelopeHelper(envelope).getCorrelationId().equals(requestId);
            }
        };

        registrationInfo = new HashMap<String, String>();
        Envelope tmpInfoEnvelope = new Envelope();
        tmpInfoEnvelope.setHeaders(registrationInfo);
        new EnvelopeHelper(tmpInfoEnvelope).setMessageTopic(buildRpcTopic(expectedTopic, requestId));
    }

    
    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() {
        return responseFilter;
    }

    @Override
    public Map<String, String> getRegistrationInfo() {
        return registrationInfo;
    }

    public Object getResponse(Duration timeout) throws TimeoutException {
        StopWatch watch = new StopWatch();
        watch.start();
        while (watch.getTime() < timeout.getMillis() && responseEvent == null) {
            try {
                Thread.sleep(Math.min(50, timeout.getMillis() / 10));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        watch.stop();

        Object response = responseEvent;
        responseEvent = null;

        return response;
    }

    @Override
    public Object handle(Envelope envelope) throws Exception {
        
    	log.debug("enter handle");
    	
    	this.responseEnvelope = envelope;
    	this.responseEvent = this.envelopeOpener.ProcessInbound(envelope);
    	log.info("inbound envelope successfully opened");
    	
    	// we have no custom response to give
    	return null;
    }

    @Override
    public Object handleFailed(Envelope envelope, Exception ex) throws Exception {
        log.error("Failed to handle an envelope: " + new EnvelopeHelper(envelope).flatten(), ex);

        return null;
    }

    
    protected void setRegistrationInfo(Map<String, String> registrationInfo) {
        this.registrationInfo = registrationInfo;
    }

    protected String buildRpcTopic(String expectedTopic, UUID requestId) {
        return String.format("%s#%s", expectedTopic, requestId.toString());
    }
}

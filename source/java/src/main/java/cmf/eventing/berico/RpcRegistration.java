package cmf.eventing.berico;

import java.util.Collection;
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

    protected Collection<IInboundEventProcessor> inboundChain;
    protected IEnvelopeFilterPredicate responseFilter;
    protected Object responseEvent;
    protected Envelope responseEnvelope;
    protected Map<String, String> registrationInfo;

    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() {
        return responseFilter;
    }

    @Override
    public Map<String, String> getRegistrationInfo() {
        return registrationInfo;
    }

    @Override
    public Object handleFailed(Envelope envelope, Exception ex) throws Exception {
        log.error("Failed to handle an envelope: " + new EnvelopeHelper(envelope).flatten(), ex);

        return null;
    }

    protected void setRegistrationInfo(Map<String, String> registrationInfo) {
        this.registrationInfo = registrationInfo;
    }

    public RpcRegistration(final UUID requestId, String expectedTopic, Collection<IInboundEventProcessor> chain) {
        inboundChain = chain;

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
    public Object handle(Envelope envelope) throws Exception {
        Object event = null;

        ProcessingContext processorContext = new ProcessingContext(envelope, event);
        if (this.processInbound(processorContext)) {
            responseEvent = processorContext.getEvent();
        }

        return null;
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

        if (responseEvent == null) {
            throw new TimeoutException();
        }

        Object response = responseEvent;
        responseEvent = null;

        return response;
    }

    protected boolean processInbound(ProcessingContext processorContext) {
        boolean processed = true;

        try {
            for (IInboundEventProcessor processor : this.inboundChain) {
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

    protected String buildRpcTopic(String expectedTopic, UUID requestId) {
        return String.format("%s#%s", expectedTopic, requestId.toString());
    }
}

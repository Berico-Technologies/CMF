package cmf.bus.eventing.berico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.bus.IRegistration;
import cmf.bus.berico.DefaultEnvelopeRegistration;
import cmf.bus.berico.HeaderEnvelopeFilterPredicate;
import cmf.bus.berico.rabbit.EnvelopeConstants;
import cmf.bus.eventing.IInboundEventProcessor;
import cmf.bus.eventing.IOutboundEventProcessor;

public class BidirectionalEventBus extends DefaultEventBus {
    
    private static final long DEFAULT_TIMEOUT = 30000; // 30 seconds

    public BidirectionalEventBus(IEnvelopeBus envelopeBus, List<IInboundEventProcessor> inboundProcessors,
                    List<IOutboundEventProcessor> outboundProcessors) {
        super(envelopeBus, inboundProcessors, outboundProcessors);
    }

    public <TRESPONSE> TRESPONSE getResponse(Object event) {
        return getResponse(event, DEFAULT_TIMEOUT);
    }

    public <TRESPONSE> TRESPONSE getResponse(Object event, long timeout) {
        IRegistration registration = new DefaultEnvelopeRegistration();
        BlockingEventHandler<TRESPONSE> blockingEventHandler = new BlockingEventHandler<TRESPONSE>(timeout);
        registration.setEnvelopeHandler(new EventBusEnvelopeHandler<TRESPONSE>(blockingEventHandler));
        Envelope envelope = new Envelope();
        processOutbound(event, envelope);
        Map<String, String> filterHeaders = new HashMap<String, String>();
        String envelopeId = envelope.getHeader(EnvelopeConstants.ID);
        filterHeaders.put(EnvelopeConstants.CORRELATION_ID, envelopeId);
        HeaderEnvelopeFilterPredicate headerEnvelopeFilterPredicate = new HeaderEnvelopeFilterPredicate(filterHeaders);
        registration.setFilterPredicate(headerEnvelopeFilterPredicate);
        envelopeBus.register(registration);
        envelopeBus.send(envelope);

        return blockingEventHandler.getEvent();
    }

    public void respondWith(Object response, Map<String, String> originalHeaders) {
        Map<String, String> filterHeaders = new HashMap<String, String>();
        String correlationId = originalHeaders.get(EnvelopeConstants.ID);
        filterHeaders.put(EnvelopeConstants.CORRELATION_ID, correlationId);
        publish(response);
    }
}

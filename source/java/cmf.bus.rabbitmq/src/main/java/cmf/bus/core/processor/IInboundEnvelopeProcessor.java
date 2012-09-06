package cmf.bus.core.processor;

import java.util.Map;

import cmf.bus.core.Envelope;

public interface IInboundEnvelopeProcessor {

    void processInbound(Envelope envelope, Map<String, Object> context);

}

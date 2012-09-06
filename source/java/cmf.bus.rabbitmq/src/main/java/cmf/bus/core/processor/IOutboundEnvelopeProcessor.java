package cmf.bus.core.processor;

import java.util.Map;

import cmf.bus.core.Envelope;

public interface IOutboundEnvelopeProcessor {

    void processOutbound(Envelope envelope, Map<String, Object> context);

}

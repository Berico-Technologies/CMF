package cmf.bus.core.processor;

import java.util.Map;

import cmf.bus.core.IEnvelope;

public interface IOutboundEnvelopeProcessor {

    void processOutbound(IEnvelope envelope, Map<String, Object> context);

}

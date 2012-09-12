package cmf.bus.berico;

import java.util.Map;

import cmf.bus.Envelope;

public interface IOutboundEnvelopeProcessor {

    void processOutbound(Envelope envelope, Map<String, Object> context);
}

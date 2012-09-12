package cmf.bus.berico;

import java.util.Map;

import cmf.bus.Envelope;

public interface IInboundEnvelopeProcessor {

    void processInbound(Envelope envelope, Map<String, Object> context);
}

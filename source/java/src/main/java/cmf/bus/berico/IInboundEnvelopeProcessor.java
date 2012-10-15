package cmf.bus.berico;

import java.util.Map;

import cmf.bus.Envelope;

public interface IInboundEnvelopeProcessor {

    boolean processInbound(Envelope envelope, Map<String, Object> context);
}

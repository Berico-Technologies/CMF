package cmf.bus.berico;

import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.IDisposable;

public interface IOutboundEnvelopeProcessor extends IDisposable {

    void processOutbound(Envelope envelope, Map<String, Object> context);
}

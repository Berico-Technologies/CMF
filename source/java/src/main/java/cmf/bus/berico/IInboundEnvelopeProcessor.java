package cmf.bus.berico;

import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.IDisposable;

public interface IInboundEnvelopeProcessor extends IDisposable {

    boolean processInbound(Envelope envelope, Map<String, Object> context);
}

package cmf.bus.default;

import cmf.bus.IDisposable;

public interface IEnvelopeProcessor extends IDisposable {
	void processEnvelope(EnvelopeContext context, IContinuationCallback continuation);
}

package cmf.bus.berico;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

public interface IEnvelopeDispatcher {

	Envelope getEnvelope();
	
	
	Object dispatch();
    Object dispatch(Envelope envelope);

    Object dispatchFailed(Envelope envelope, Exception e);
}

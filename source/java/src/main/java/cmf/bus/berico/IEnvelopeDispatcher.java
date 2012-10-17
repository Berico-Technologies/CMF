package cmf.bus.berico;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

public interface IEnvelopeDispatcher {

	Envelope getEnvelope();
	
	
	void dispatch();
    void dispatch(Envelope envelope);

    void dispatchFailed(Envelope envelope, Exception e);
}

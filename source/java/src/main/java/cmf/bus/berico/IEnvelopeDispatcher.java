package cmf.bus.berico;

import cmf.bus.Envelope;

public interface IEnvelopeDispatcher {

	Envelope getEnvelope();
	
	
	void dispatch();
    void dispatch(Envelope envelope);

    void dispatchFailed(Envelope envelope, Exception e);
}

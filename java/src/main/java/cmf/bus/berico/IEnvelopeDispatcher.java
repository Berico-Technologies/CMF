package cmf.bus.berico;

import cmf.bus.Envelope;

public interface IEnvelopeDispatcher {

    void dispatch();

    void dispatch(Envelope envelope);

    void dispatchFailed(Envelope envelope, Exception e);

    Envelope getEnvelope();
}

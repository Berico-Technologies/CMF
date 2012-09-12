package cmf.bus.berico;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

public interface IEnvelopeDispatcher {

    Object dispatch(IRegistration registration, Envelope envelope);

    Object dispatchFailed(IRegistration registration, Envelope envelope, Exception e);
}

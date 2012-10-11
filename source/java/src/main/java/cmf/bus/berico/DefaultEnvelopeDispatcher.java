package cmf.bus.berico;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

public class DefaultEnvelopeDispatcher implements IEnvelopeDispatcher {

    @Override
    public Object dispatch(IRegistration registration, Envelope envelope) {
        Object result = null;
        if (registration.getFilterPredicate().filter(envelope)) {
            result = registration.getHandler().handle(envelope);
        }

        return result;
    }

    @Override
    public Object dispatchFailed(IRegistration registration, Envelope envelope, Exception e) {
        return registration.getHandler().handleFailed(envelope, e);
    }
}

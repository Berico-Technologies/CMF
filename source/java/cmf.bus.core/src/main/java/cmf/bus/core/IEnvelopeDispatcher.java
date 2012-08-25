package cmf.bus.core;

import java.util.Collection;

public interface IEnvelopeDispatcher {

    void dispatch(IEnvelope envelope, Collection<IRegistration> registeredHandlers);

}

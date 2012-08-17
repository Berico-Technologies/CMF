package cmf.bus.core.transport;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.IEnvelopeHandler;
import cmf.bus.core.IRegistration;

public interface ITransportProvider {

    IRegistration register(IRoute route, IEnvelopeHandler envelopeHandler);

    void unregister(IRegistration registration);

    void send(IRoute route, IEnvelope envelope);

    boolean canSendToRoute(IRoute route);

    boolean canReceiveFromRoute(IRoute route);

}

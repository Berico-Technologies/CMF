package cmf.bus.core.transport;

import java.util.Collection;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.IEnvelopeHandler;
import cmf.bus.core.IRegistration;

public interface ITransportManager {

    IRegistration register(String registrationKey, IEnvelopeHandler envelopeHandler);

    void unregister(IRegistration registration);

    void send(IEnvelope envelope);

    void setTopologyProvider(ITopologyProvider topologyProvider);

    void setTransportProviderCollection(Collection<ITransportProvider> transportProviderCollection);

}

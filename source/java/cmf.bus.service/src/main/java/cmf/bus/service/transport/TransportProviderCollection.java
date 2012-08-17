package cmf.bus.service.transport;

import java.util.Collection;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.IReceiveHandler;
import cmf.bus.core.IRegistration;
import cmf.bus.core.IRoute;
import cmf.bus.core.ITransportProvider;

public class TransportProviderCollection implements ITransportProvider {

    private Collection<ITransportProvider> transportProviderCollection;

    public TransportProviderCollection() {

    }

    public void setTransportProvider(Collection<ITransportProvider> transportProviderCollection) {
        this.transportProviderCollection = transportProviderCollection;
    }

    @Override
    public void send(IEnvelope envelope) {
        for (ITransportProvider transportProvider : transportProviderCollection) {
            transportProvider.send(envelope);
        }
    }

    @Override
    public IRegistration register(String registrationKey, IReceiveHandler receiveHandler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void unregister(IRegistration registration) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean canSendToRoute(IRoute route) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canReceiveFromRoute(IRoute route) {
        // TODO Auto-generated method stub
        return false;
    }

}

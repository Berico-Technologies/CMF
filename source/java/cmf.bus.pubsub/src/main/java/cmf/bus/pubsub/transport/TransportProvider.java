package cmf.bus.pubsub.transport;

import java.util.Collection;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.IRegistration;
import cmf.bus.core.ITransportProvider;
import cmf.bus.pubsub.Envelope;
import cmf.bus.pubsub.Registration;

public class TransportProvider implements ITransportProvider {

    private IBroker broker;
    private TopologyProvider topologyProvider;

    @Override
    public void register(IRegistration registration) {
        if (!(registration instanceof Registration)) {
            throw new IllegalArgumentException("Registration must be of type PubSubRegistration");
        }
        Collection<Route> routes = topologyProvider.getReceiveRoutes((Registration) registration);
        broker.register((Registration) registration, routes);
    }

    @Override
    public void send(IEnvelope envelope) {
        if (!(envelope instanceof Envelope)) {
            throw new IllegalArgumentException("Envelope must be of type PubSubEnvelope");
        }
        Collection<Route> routes = topologyProvider.getSendRoutes((Envelope) envelope);
        broker.send((Envelope) envelope, routes);
    }

    public void setBroker(IBroker broker) {
        this.broker = broker;
    }

    public void setTopologyProvider(TopologyProvider topologyProvider) {
        this.topologyProvider = topologyProvider;
    }

}

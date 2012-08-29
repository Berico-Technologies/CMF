package cmf.bus.rabbitmq.transport;

import java.util.Collection;

import cmf.bus.core.Envelope;
import cmf.bus.core.IEnvelope;
import cmf.bus.core.IRegistration;
import cmf.bus.core.Registration;
import cmf.bus.core.transport.ITransportProvider;

public class TransportProvider implements ITransportProvider {

    private Broker broker;
    private TopologyProvider topologyProvider;

    @Override
    public void register(IRegistration registration) {
        Collection<Route> routes = topologyProvider.getReceiveRouteCollection((Registration) registration);
        broker.register(registration, routes);
    }

    @Override
    public void send(IEnvelope envelope) {
        Collection<Route> routes = topologyProvider.getSendRouteCollection((Envelope) envelope);
        broker.send(envelope, routes);
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public void setTopologyProvider(TopologyProvider topologyProvider) {
        this.topologyProvider = topologyProvider;
    }

}

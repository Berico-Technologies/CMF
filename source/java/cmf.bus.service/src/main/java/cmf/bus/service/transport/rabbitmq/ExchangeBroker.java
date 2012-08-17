package cmf.bus.service.transport.rabbitmq;

import java.util.Collection;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.IReceiveHandler;
import cmf.bus.core.IReceiveModuleCollection;
import cmf.bus.core.IRegistration;
import cmf.bus.core.IRoute;
import cmf.bus.core.ITopologyProvider;
import cmf.bus.core.ITransportProvider;

public class ExchangeBroker implements ITransportProvider {

    private ITopologyProvider topologyProvider;
    private IReceiveModuleCollection receiveModuleCollection;

    public ExchangeBroker() {

    }

    public void setReceiveModuleCollection(IReceiveModuleCollection receiveModuleCollection) {
        this.receiveModuleCollection = receiveModuleCollection;
    }

    public void setTopologyProvider(ITopologyProvider topologyProvider) {
        this.topologyProvider = topologyProvider;
    }

    @Override
    public IRegistration register(String messageType, IReceiveHandler receiveHandler) {
        Collection<IRoute> routes = topologyProvider.getReceiveRoutes(messageType);
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void unregister(IRegistration registration) {
        // TODO Auto-generated method stub

    }

    @Override
    public void send(IEnvelope envelope) {
        String messageType = envelope.getHeader(IEnvelope.MESSAGE_TYPE);
        Collection<IRoute> routes = topologyProvider.getSendRoutes(messageType);
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

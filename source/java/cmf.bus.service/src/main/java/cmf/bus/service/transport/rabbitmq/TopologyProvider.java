package cmf.bus.service.transport.rabbitmq;

import java.util.Collection;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.IReceiveHandler;
import cmf.bus.core.IRegistration;
import cmf.bus.core.IRoute;
import cmf.bus.core.ITopologyProvider;

public class TopologyProvider implements ITopologyProvider {

    public TopologyProvider() {

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
    public void send(IEnvelope envelope) {
        // TODO Auto-generated method stub

    }

    @Override
    public Collection<IRoute> getSendRoutes(String registrationKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<IRoute> getReceiveRoutes(String registrationKey) {
        // TODO Auto-generated method stub
        return null;
    }

}

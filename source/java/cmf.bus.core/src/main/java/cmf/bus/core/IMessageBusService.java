package cmf.bus.core;

import java.util.Collection;

import cmf.bus.core.module.IReceiveModule;
import cmf.bus.core.module.ISendModule;
import cmf.bus.core.transport.ITransportManager;

public interface IMessageBusService {

    IRegistration register(String registrationKey, IEnvelopeHandler envelopeHandler);

    void unregister(IRegistration registration);

    void send(IEnvelope envelope);

    void setReceiveModuleCollection(Collection<IReceiveModule> receiveModuleCollection);

    void setSendModuleCollection(Collection<ISendModule> sendModuleCollection);

    void setTransportManager(ITransportManager transportManager);

}

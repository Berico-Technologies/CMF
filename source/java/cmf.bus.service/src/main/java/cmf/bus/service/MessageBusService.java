package cmf.bus.service;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.IReceiveHandler;
import cmf.bus.core.IMessageBusService;
import cmf.bus.core.IRegistration;
import cmf.bus.core.ISendModule;
import cmf.bus.core.ITransportProvider;

public class MessageBusService implements IMessageBusService {

    private ISendModule sendModule;
    private ITransportProvider transportProvider;

    public MessageBusService() {

    }

    @Override
    public IRegistration register(String registrationKey, IReceiveHandler receiveHandler) {
        return transportProvider.register(registrationKey, receiveHandler);
    }

    @Override
    public void unregister(IRegistration registration) {
        transportProvider.unregister(registration);
    }

    @Override
    public void send(IEnvelope envelope) {
        sendModule.send(envelope);
        transportProvider.send(envelope);
    }

    public void setSendModule(ISendModule sendModule) {
        this.sendModule = sendModule;
    }

    public void setTransportProvider(ITransportProvider transportProvider) {
        this.transportProvider = transportProvider;
    }

}

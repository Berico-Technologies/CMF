package cmf.bus.service.transport.rabbitmq;

import cmf.bus.core.IReceiveHandler;
import cmf.bus.core.IRegistration;

public class QueueConsumer implements IRegistration {

    private String registrationKey;
    private IReceiveHandler receiveHandler;

    public QueueConsumer() {

    }

    @Override
    public String getRegistrationKey() {
        return registrationKey;
    }

    @Override
    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }

    @Override
    public IReceiveHandler getReceiveHandler() {
        return receiveHandler;
    }

    @Override
    public void setReceiveHandler(IReceiveHandler receiveHandler) {
        this.receiveHandler = receiveHandler;
    }

}

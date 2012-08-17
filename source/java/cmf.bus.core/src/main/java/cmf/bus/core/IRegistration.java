package cmf.bus.core;

public interface IRegistration {

    String getRegistrationKey();

    void setRegistrationKey(String registrationKey);

    IReceiveHandler getReceiveHandler();

    void setReceiveHandler(IReceiveHandler receiveHandler);

}

package cmf.bus.core;

public interface IRegistrationHandler {

    IRegistration register(String registrationKey, IReceiveHandler receiveHandler);

    void unregister(IRegistration registration);

}

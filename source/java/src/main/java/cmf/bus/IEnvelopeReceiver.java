package cmf.bus;

public interface IEnvelopeReceiver {

    void register(IRegistration registration);
    
    void unregister(IRegistration registration);
}

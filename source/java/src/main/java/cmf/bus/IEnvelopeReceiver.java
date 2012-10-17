package cmf.bus;

public interface IEnvelopeReceiver {

    void register(IRegistration registration) throws Exception;
    
    void unregister(IRegistration registration) throws Exception;
}

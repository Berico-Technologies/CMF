package cmf.bus;

public interface IEnvelopeReceiver extends IDisposable {

    void register(IRegistration registration) throws Exception;
    
    void unregister(IRegistration registration) throws Exception;
}

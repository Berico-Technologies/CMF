package cmf.bus.core;

public interface IEnvelopeBus {

    void register(IRegistration registration);

    void send(IEnvelope envelope);

}

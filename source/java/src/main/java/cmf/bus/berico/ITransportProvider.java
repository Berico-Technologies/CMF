package cmf.bus.berico;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

public interface ITransportProvider {

    void register(IRegistration registration, IEnvelopeReceivedCallback callback);

    void unregister(IRegistration registration);

    void send(Envelope envelope);
}

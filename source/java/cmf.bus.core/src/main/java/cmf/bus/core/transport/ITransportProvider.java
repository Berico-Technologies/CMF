package cmf.bus.core.transport;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.IRegistration;

public interface ITransportProvider {

    void register(IRegistration registration);

    void send(IEnvelope envelope);

}

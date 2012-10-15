package cmf.bus.berico;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

public interface ITransportProvider {

	void onEnvelopeReceived(IEnvelopeReceivedCallback callback);
	
	
    void register(IRegistration registration);

    void unregister(IRegistration registration);

    void send(Envelope envelope);
}

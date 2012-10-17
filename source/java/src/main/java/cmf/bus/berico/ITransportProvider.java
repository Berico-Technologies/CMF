package cmf.bus.berico;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

public interface ITransportProvider {

	void onEnvelopeReceived(IEnvelopeReceivedCallback callback);
	
	
    void register(IRegistration registration) throws Exception;

    void unregister(IRegistration registration) throws Exception;

    void send(Envelope envelope) throws Exception;
}

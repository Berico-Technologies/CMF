package cmf.bus.berico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;

public class InboundEnvelopeProcessorCallback implements IEnvelopeReceivedCallback {

	private static final Logger log = LoggerFactory.getLogger(InboundEnvelopeProcessorCallback.class);
	
	private DefaultEnvelopeBus envelopeBus;
	
	public InboundEnvelopeProcessorCallback(DefaultEnvelopeBus envelopeBus) {
		
		this.envelopeBus = envelopeBus;
	}
	
	@Override
	public void handleReceive(IEnvelopeDispatcher dispatcher) {
		
		log.debug("Got an envelope dispatcher from the transport provider");
        Envelope env = dispatcher.getEnvelope();

        try {
            // send the envelope through the inbound processing chain
            if (this.envelopeBus.processInbound(env)) {
                // the dispatcher encapsulates the logic of giving the envelope to handlers
                dispatcher.dispatch(env);

                log.debug("Dispatched envelope");
            }
        } catch (Exception ex) {
        	
            log.warn("Failed to dispatch envelope; raising EnvelopeFailed event: {}", ex);
            dispatcher.dispatchFailed(env, ex);
        }
	}
}
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
		
		
	}
}
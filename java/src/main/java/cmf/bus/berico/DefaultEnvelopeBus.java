package cmf.bus.berico;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.bus.IRegistration;
import cmf.bus.berico.EnvelopeContext.Directions;

public class DefaultEnvelopeBus implements IEnvelopeBus, IEnvelopeReceivedCallback {

	protected final Logger log = LoggerFactory.getLogger(this.getClass().getCanonicalName());
	
    protected List<IEnvelopeProcessor> _inboundProcessors = new LinkedList<IEnvelopeProcessor>();
    protected List<IEnvelopeProcessor> _outboundProcessors = new LinkedList<IEnvelopeProcessor>();
    protected ITransportProvider _transportProvider;

    
    public void setInboundProcessors(List<IEnvelopeProcessor> inboundChain) {
    	_inboundProcessors = inboundChain;
    }
    
    public void setOutboundProcessors(List<IEnvelopeProcessor> outboundChain) {
    	_outboundProcessors = outboundChain;
    }
    
    
    public DefaultEnvelopeBus(ITransportProvider transportProvider) {
        _transportProvider = transportProvider;

        initialize();
    }

    public DefaultEnvelopeBus(
		ITransportProvider transportProvider, 
		List<IEnvelopeProcessor> inboundChain,
        List<IEnvelopeProcessor> outboundChain)
    {
        this._transportProvider = transportProvider;
        this._inboundProcessors = inboundChain;
        this._outboundProcessors = outboundChain;

        initialize();
    }

    
    @Override
    public void dispose() {
        _transportProvider.dispose();

        for (IEnvelopeProcessor p : _inboundProcessors) {
            try {
                p.dispose();
            } catch (Exception ex) {
            }
        }

        for (IEnvelopeProcessor p : _outboundProcessors) {
            try {
                p.dispose();
            } catch (Exception ex) {
            }
        }
    }

    public void processEnvelope(
    		final EnvelopeContext context, 
    		final List<IEnvelopeProcessor> processingChain, 
    		final IContinuationCallback onComplete) throws Exception
    {
    	log.debug("Enter processEnvelope");
    	
    	// if the chain is null or empty, complete processing
    	if ( (null == processingChain) || (0 == processingChain.size()) ) {
    		log.debug("envelope processing complete");
    		onComplete.continueProcessing();
    		return;
    	}
    	
    	// get the first processor
    	IEnvelopeProcessor processor = processingChain.get(0);
    	
    	// create a processing chain that no longer contains this processor
    	final List<IEnvelopeProcessor> newChain = processingChain.subList(1, processingChain.size());
    	
    	// let it process the envelope and pass its "next" processor: a method that
    	// recursively calls this function with the current processor removed
    	processor.processEnvelope(context, new IContinuationCallback() {

			@Override
			public void continueProcessing() throws Exception {
				processEnvelope(context, newChain, onComplete);
			}
    		
    	});
    	
    	log.debug("Leave processEnvelope");
    }
    
    @Override
    public void register(final IRegistration registration) throws Exception {
        log.debug("Enter register");
        if (registration == null) {
            throw new IllegalArgumentException("Cannot register with a null registration");
        }

        _transportProvider.register(registration);
        log.debug("Leave register");
    }

    @Override
    public void send(final Envelope envelope) throws Exception {
    	
        log.debug("Enter send");
        
        // check for null envelopes
        if (envelope == null) {
            throw new IllegalArgumentException("Cannot send a null envelope");
        }

        // create an envelope context that will be passed among the processors
        final EnvelopeContext context = new EnvelopeContext(Directions.Out, envelope);
        
        // send the envelope through the outbound processing chain
        this.processEnvelope(context, _outboundProcessors, new IContinuationCallback() {

			@Override
			public void continueProcessing() throws Exception {
				_transportProvider.send(context.getEnvelope());
				log.debug("Outgoing headers: {}", new EnvelopeHelper(context.getEnvelope()).flatten());
			}
        	
        });
        
        log.debug("Leave send");
    }

    @Override
    public void unregister(IRegistration registration) throws Exception {
        log.debug("Enter unregister");

        if (registration == null) {
            throw new IllegalArgumentException("Cannot unregister with a null registration");
        }
        _transportProvider.unregister(registration);

        log.debug("Leave unregister");
    }

    @Override
	public void handleReceive(final IEnvelopeDispatcher dispatcher) {
    	
    	log.debug("Got an envelope dispatcher from the transport provider");
    	final EnvelopeContext context = new EnvelopeContext(Directions.In, dispatcher.getEnvelope());

        try {
            // send the envelope through the inbound processing chain
            this.processEnvelope(context, _inboundProcessors, new IContinuationCallback() {

                @Override
                public void continueProcessing() throws Exception {
                    // the dispatcher encapsulates the logic of giving the envelope to handlers
                    dispatcher.dispatch(context.getEnvelope());

                    log.debug("Dispatched envelope");
                }  
            });
        } catch (Exception ex) {
        	
            log.warn("Failed to dispatch envelope; raising EnvelopeFailed event: {}", ex);
            dispatcher.dispatchFailed(context.getEnvelope(), ex);
        }
	}

    
    void initialize() {

    	// add a handler to the transport provider's envelope received event
        _transportProvider.onEnvelopeReceived(this);

        log.debug("Initialized");
    }
}

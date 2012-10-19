package cmf.bus.berico;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.bus.IRegistration;

public class DefaultEnvelopeBus implements IEnvelopeBus {

    protected List<IInboundEnvelopeProcessor> inboundProcessors = new LinkedList<IInboundEnvelopeProcessor>();
    protected List<IOutboundEnvelopeProcessor> outboundProcessors = new LinkedList<IOutboundEnvelopeProcessor>();
    protected ITransportProvider transportProvider;
    protected final Logger log = LoggerFactory.getLogger(this.getClass().getCanonicalName());

    
    public DefaultEnvelopeBus(ITransportProvider transportProvider) {
        this.transportProvider = transportProvider;
        
        this.initialize();
    }

    public DefaultEnvelopeBus(
    		ITransportProvider transportProvider, 
    		List<IInboundEnvelopeProcessor> inboundProcessors,
            List<IOutboundEnvelopeProcessor> outboundProcessors) {
        this.transportProvider = transportProvider;
        this.inboundProcessors = inboundProcessors;
        this.outboundProcessors = outboundProcessors;
        
        this.initialize();
    }

    
    protected void initialize() {
    	
    	// add a handler to the transport provider's envelope received event
        this.transportProvider.onEnvelopeReceived(new IEnvelopeReceivedCallback() {

        	// implement the callback method
            public void handleReceive(IEnvelopeDispatcher dispatcher) {
            	
            	log.debug("Got an envelope dispatcher from the transport provider");
            	Envelope env = dispatcher.getEnvelope();
            	
            	try
                {
                    // send the envelope through the inbound processing chain
                    if (processInbound(env))
                    {
                        // the dispatcher encapsulates the logic of giving the envelope to handlers
                        dispatcher.dispatch(env);

                        log.debug("Dispatched envelope");
                    }
                }
                catch (Exception ex)
                {
                    log.warn("Failed to dispatch envelope; raising EnvelopeFailed event");
                    dispatcher.dispatchFailed(env, ex);
                }
            } // end of the callback method
        }); // end of adding event handler
        
        this.log.debug("Initialized");
    }
    
    protected boolean processInbound(Envelope envelope) {
    	this.log.debug("Enter processInbound");
    	
    	// the inbound process can be cancelled if one of the processors returns false
    	boolean noOneCancelled = true;
    	
        Map<String, Object> context = new HashMap<String, Object>();
        for (IInboundEnvelopeProcessor inboundEnvelopeProcessor : inboundProcessors) {
            
        	if (inboundEnvelopeProcessor.processInbound(envelope, context)) {
        		continue;
        	}
        	else {
        		this.log.info("Inbound envelope cancelled by processor of type {}", 
        				inboundEnvelopeProcessor.getClass().getCanonicalName());
        		
        		noOneCancelled = false;
        		break;
        	}
        }
        
        this.log.debug("Leave processInbound(noOneCancelled={})", noOneCancelled);
        return noOneCancelled;
    }

    protected void processOutbound(Envelope envelope) {
    	this.log.debug("Enter processOutbound");
    	
        Map<String, Object> context = new HashMap<String, Object>();
        for (IOutboundEnvelopeProcessor outboundEnvelopeProcessor : outboundProcessors) {
            outboundEnvelopeProcessor.processOutbound(envelope, context);
        }
        
        this.log.debug("Leave processOutbound");
    }

    
    @Override
    public void register(final IRegistration registration) throws Exception {
    	this.log.debug("Enter register");
        if (registration == null) {
            throw new IllegalArgumentException("Cannot register with a null registration");
        }

        transportProvider.register(registration);
        this.log.debug("Leave register");
    }

    @Override
    public void send(Envelope envelope) throws Exception {
    	this.log.debug("Enter send");
        if (envelope == null) {
            throw new IllegalArgumentException("Cannot send a null envelope");
        }
        
        processOutbound(envelope);
        transportProvider.send(envelope);
        
        this.log.debug("Outgoing headers: {}", new EnvelopeHelper(envelope).flatten());
        this.log.debug("Leave send");
    }

    @Override
    public void unregister(IRegistration registration) throws Exception {
    	this.log.debug("Enter unregister");
    	
        if (registration == null) {
            throw new IllegalArgumentException("Cannot unregister with a null registration");
        }
        transportProvider.unregister(registration);
        
        this.log.debug("Leave unregister");
    }
    
    public void setInboundProcessors(List<IInboundEnvelopeProcessor> inboundProcessors) {
        this.inboundProcessors = inboundProcessors;
    }

    public void setOutboundProcessorCollection(List<IOutboundEnvelopeProcessor> outboundProcessors) {
        this.outboundProcessors = outboundProcessors;
    }
}

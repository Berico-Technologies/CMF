package cmf.eventing.berico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.joda.time.Duration;
import org.slf4j.Logger;

import cmf.bus.Envelope;
import cmf.eventing.IInboundEventProcessor;
import cmf.eventing.IOutboundEventProcessor;

public class RpcFilter implements IInboundEventProcessor, IOutboundEventProcessor {

	protected List<UUID> sentRequests;
    protected Logger log;
    
    
    public RpcFilter() {
    	this.sentRequests = new ArrayList<UUID>();
    }
    
    
	@Override
	public void processOutbound(Object event, Envelope envelope,
			Map<String, Object> context) {

		cmf.bus.berico.EnvelopeHelper env = new cmf.bus.berico.EnvelopeHelper(envelope);
		
        if (env.IsRequest())
        {
            final UUID requestId = env.getMessageId();
            Duration timeout = env.getRpcTimeout();

            synchronized(this) {
                log.debug(String.format("Adding requestId %s to the RPC Filter list", requestId.toString()));
                sentRequests.add(requestId);
            }

            if (timeout == Duration.ZERO)
            {
                Timer gc = new Timer(true);
                gc.schedule(new TimerTask() {

					@Override
					public void run() {
						requestTimeout_GarbageCollect(requestId);
					}
                	
                	}, 
                	timeout.getMillis()*2);
            }
            else
            {
                log.warn(String.format(
                    "Request %s was sent without a timeout: it will never be removed from the RPC Filter list", 
                    requestId.toString()));
            }
        }
	}

	@Override
	public boolean processInbound(Object event, Envelope envelope,
			Map<String, Object> context) {
        
		boolean ourOwnRequest = false;
		cmf.bus.berico.EnvelopeHelper env = new cmf.bus.berico.EnvelopeHelper(envelope);
        
        try
        {
            if (env.IsRequest())
            {
                UUID requestId = env.getMessageId();

                synchronized(this) {
                    if (sentRequests.contains(requestId))
                    {
                        log.info("Filtering out our own request: " + requestId.toString());
                        ourOwnRequest = true;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            log.error("Failed to inspect an incoming event for potential filtering", ex);
        }

        return !ourOwnRequest;
	}
	
    public void requestTimeout_GarbageCollect(UUID requestId)
    {
        synchronized (this) {
            log.debug(String.format("Removing requestId %s from the RPC Filter list", requestId.toString()));
            sentRequests.remove(requestId);
        }
    }
}

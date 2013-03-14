package cmf.eventing.default.serializers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.eventing.default.EnvelopeHelper;
import cmf.eventing.default.EventContext;
import cmf.eventing.default.EventContext.Directions;
import cmf.eventing.default.IContinuationCallback;
import cmf.eventing.default.IEventProcessor;
import cmf.eventing.default.ISerializer;


public class JsonEventSerializer implements IEventProcessor {

    protected Logger log;
    protected ISerializer serializer;

    
    public JsonEventSerializer(ISerializer serializer) {
        this.serializer = serializer;
        log = LoggerFactory.getLogger(this.getClass());
    }

    
    @Override
    public void processEvent(EventContext context, IContinuationCallback continuation) throws Exception {
    	
    	if (Directions.In == context.getDirection()) {
    		this.processInbound(context, continuation);
    	}
    	else if (Directions.Out == context.getDirection()) {
    		this.processOutbound(context, continuation);
    	}
    }
    
    
    public void processInbound(EventContext context, IContinuationCallback continuation) throws Exception {

        boolean success = false;
        EnvelopeHelper env = new EnvelopeHelper(context.getEnvelope());

        try {
            String eventType = env.getMessageType();

            Class<?> type = Class.forName(eventType);
            context.setEvent(serializer.byteDeserialize(env.getPayload(), type));

            success = true;
        } catch (Exception e) {
            log.error("Error deserializing event", e);
            throw new RuntimeException("Error deserializing event");
        }

        if (success) { continuation.continueProcessing(); }
    }

    public void processOutbound(EventContext context, IContinuationCallback continuation) throws Exception {

        context.getEnvelope().setPayload(serializer.byteSerialize(context.getEvent()));
        continuation.continueProcessing();
    }
}

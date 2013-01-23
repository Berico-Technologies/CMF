package cmf.eventing.berico.serializers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.berico.EnvelopeHelper;
import cmf.eventing.berico.IInboundEventProcessor;
import cmf.eventing.berico.IOutboundEventProcessor;
import cmf.eventing.berico.ISerializer;
import cmf.eventing.berico.ProcessingContext;

public class JsonEventSerializer implements IInboundEventProcessor, IOutboundEventProcessor {

    protected Logger log;
    protected ISerializer serializer;

    public JsonEventSerializer(ISerializer serializer) {
        this.serializer = serializer;
        log = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public boolean processInbound(ProcessingContext context) {

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

        return success;
    }

    @Override
    public void processOutbound(ProcessingContext context) {

        context.getEnvelope().setPayload(serializer.byteSerialize(context.getEvent()));
    }
}

package cmf.eventing.berico.serializers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.bus.berico.EnvelopeHelper;
import cmf.eventing.IInboundEventProcessor;
import cmf.eventing.IOutboundEventProcessor;
import cmf.eventing.berico.ISerializer;

public class JsonEventSerializer implements IInboundEventProcessor, IOutboundEventProcessor {

	protected Logger log;
	protected ISerializer serializer;
	
	
	public JsonEventSerializer(ISerializer serializer) {
		this.serializer = serializer;
		log = LoggerFactory.getLogger(this.getClass());
	}
	
	
	@Override
	public void processOutbound(Object event, Envelope envelope,
			Map<String, Object> context) {
			
		envelope.setPayload(this.serializer.byteSerialize(event));
	}

	@Override
	public boolean processInbound(Object event, Envelope envelope,
			Map<String, Object> context) {
		
		boolean success = false;
		
		try {
            String eventType = new EnvelopeHelper(envelope).getMessageType();
            Class<?> type = Class.forName(eventType);
            event = serializer.byteDeserialize(envelope.getPayload(), type);
            success = true;
        } catch (Exception e) {
        	log.error("Error deserializing event", e);
            throw new RuntimeException("Error deserializing event");
        }
		
		return success;
	}
}

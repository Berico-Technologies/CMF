package cmf.eventing.berico;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import cmf.bus.Envelope;
import cmf.eventing.IOutboundEventProcessor;

public class OutboundHeadersProcessor implements IOutboundEventProcessor {

	@Override
	public void processOutbound(
			Object event, 
			Envelope envelope,
			Map<String, Object> context) {
		
		cmf.bus.berico.EnvelopeHelper env = new cmf.bus.berico.EnvelopeHelper(envelope);
		
        UUID messageId = env.getMessageId();
        messageId = (null == messageId) ? UUID.randomUUID() : messageId;
        env.setMessageId(messageId);

        UUID correlationId = env.getCorrelationId();

        String messageType = env.getMessageType();
        messageType = StringUtils.isBlank(messageType) ? event.getClass().getCanonicalName() : messageType;
        env.setMessageType(messageType);

        String messageTopic = env.getMessageTopic();
        messageTopic = StringUtils.isBlank(messageTopic) ? event.getClass().getCanonicalName() : messageTopic;
        if (null != correlationId)
        {
            messageTopic = messageTopic + "#" + correlationId.toString();
        }
        env.setMessageTopic(messageTopic); 
	}

}

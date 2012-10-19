package cmf.eventing.berico;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import cmf.bus.berico.EnvelopeHelper;

public class OutboundHeadersProcessor implements IOutboundEventProcessor {

	@Override
	public void processOutbound(ProcessingContext context) {
		
		EnvelopeHelper env = new EnvelopeHelper(context.getEnvelope());
		
        UUID messageId = env.getMessageId();
        messageId = (null == messageId) ? UUID.randomUUID() : messageId;
        env.setMessageId(messageId);

        UUID correlationId = env.getCorrelationId();

        String messageType = env.getMessageType();
        messageType = StringUtils.isBlank(messageType) ? context.getEvent().getClass().getCanonicalName() : messageType;
        env.setMessageType(messageType);

        String messageTopic = env.getMessageTopic();
        messageTopic = StringUtils.isBlank(messageTopic) ? context.getEvent().getClass().getCanonicalName() : messageTopic;
        if (null != correlationId)
        {
            messageTopic = messageTopic + "#" + correlationId.toString();
        }
        env.setMessageTopic(messageTopic); 
	}

}

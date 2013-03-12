package cmf.eventing.default;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import cmf.bus.berico.EnvelopeHelper;
import cmf.eventing.berico.EventContext.Directions;
import cmf.eventing.Event;
import cmf.security.IUserInfoRepository;

public class OutboundHeadersProcessor implements IEventProcessor {

	protected IUserInfoRepository userInfoRepo;
	
	
	public OutboundHeadersProcessor(IUserInfoRepository userInfoRepository) {
		this.userInfoRepo = userInfoRepository;
	}
	
	
	@Override
	public void processEvent(EventContext context, IContinuationCallback continuation) throws Exception {
		if (Directions.Out == context.getDirection()) {
			processOutbound(context, continuation);
		}
	}
	
	public void processOutbound(EventContext context, IContinuationCallback continuation) throws Exception {
		
		EnvelopeHelper env = new EnvelopeHelper(context.getEnvelope());
		
        UUID messageId = env.getMessageId();
        messageId = (null == messageId) ? UUID.randomUUID() : messageId;
        env.setMessageId(messageId);

        UUID correlationId = env.getCorrelationId();

        String messageType = env.getMessageType();
        messageType = StringUtils.isBlank(messageType) ? this.getMessageType(context.getEvent()) : messageType;
        env.setMessageType(messageType);

        String messageTopic = env.getMessageTopic();
        messageTopic = StringUtils.isBlank(messageTopic) ? this.getMessageTopic(context.getEvent()) : messageTopic;
        if (null != correlationId)
        {
            messageTopic = messageTopic + "#" + correlationId.toString();
        }
        env.setMessageTopic(messageTopic);
        
        String senderIdentity = env.getSenderIdentity();
        senderIdentity = StringUtils.isBlank(senderIdentity) ? userInfoRepo.getDistinguishedName(System.getProperty("user.name")).replaceAll(",", ", ") : senderIdentity;
        env.setSenderIdentity(senderIdentity);
        
        continuation.continueProcessing();
	}
	
	
	protected String getMessageType(Object event) {
		
		String messageType = event.getClass().getCanonicalName();
		
		Event eventMetadata = event.getClass().getAnnotation(Event.class);
		
		if (null != eventMetadata) {
			messageType = eventMetadata.type();
		}
		
		return messageType;
	}
	
	protected String getMessageTopic(Object event) {
		
		String messageTopic = event.getClass().getCanonicalName();
		
		Event eventMetadata = event.getClass().getAnnotation(Event.class);
		
		if (null != eventMetadata) {
			messageTopic = eventMetadata.topic();
		}
		
		return messageTopic;
	}
}

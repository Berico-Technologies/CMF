package cmf.eventing.berico;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import cmf.bus.berico.EnvelopeHelper;
import cmf.security.IUserInfoRepository;

public class OutboundHeadersProcessor implements IOutboundEventProcessor {

	protected IUserInfoRepository userInfoRepo;
	
	
	public OutboundHeadersProcessor(IUserInfoRepository userInfoRepository) {
		this.userInfoRepo = userInfoRepository;
	}
	
	
	@Override
	public void processOutbound(ProcessingContext context) throws Exception {
		
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
        
        String senderIdentity = env.getSenderIdentity();
        senderIdentity = StringUtils.isBlank(senderIdentity) ? userInfoRepo.getDistinguishedName(System.getProperty("user.name")).replaceAll(",", ", ") : senderIdentity;
        env.setSenderIdentity(senderIdentity);
	}
}

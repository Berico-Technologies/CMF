package cmf.bus.berico;

import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import cmf.bus.Envelope;

public class EnvelopeHelper {

	private Envelope env;
	
	
	public EnvelopeHelper(Envelope envelope) {
		this.env = envelope;
	}
	
	
	public String getMessageTopic() {
		return env.getHeader(EnvelopeHeaderConstants.MESSAGE_TOPIC);
	}
	
	public void setMessageTopic(String topic) {
		env.setHeader(EnvelopeHeaderConstants.MESSAGE_TOPIC, topic);
	}
	
	
	public UUID getMessageId() {
		UUID id = null;
		
		String idString = env.getHeader(EnvelopeHeaderConstants.MESSAGE_ID);
		id = UUID.fromString(idString);
		
		return id;
	}
	
	public void setMessageId(UUID id) {
		env.setHeader(EnvelopeHeaderConstants.MESSAGE_ID, id.toString());
	}
	
	
	public UUID getCorrelationId() {
		UUID cid = null;
		
		String cidString = env.getHeader(EnvelopeHeaderConstants.MESSAGE_CORRELATION_ID);
		cid = UUID.fromString(cidString);
		
		return cid;
	}
	
	public void setCorrelationId(UUID cid) {
		env.setHeader(EnvelopeHeaderConstants.MESSAGE_CORRELATION_ID, cid.toString());
	}
	
	
	public String getMessageType() {
		return env.getHeader(EnvelopeHeaderConstants.MESSAGE_TYPE);
	}

	public void setMessageType(String messageType) {
		env.setHeader(EnvelopeHeaderConstants.MESSAGE_TYPE, messageType);
	}
	
	
	public String getMessagePattern() {
		return env.getHeader(EnvelopeHeaderConstants.MESSAGE_PATTERN);
	}
	
	public void setMessagePattern(String pattern) {
		env.setHeader(EnvelopeHeaderConstants.MESSAGE_PATTERN, pattern);
	}
	
	
	public Duration getRpcTimeout() {
		String timeString = env.getHeader(EnvelopeHeaderConstants.MESSAGE_PATTERN_RPC_TIMEOUT);
		if (null == timeString) {
			return Duration.ZERO;
		}
		
		long totalMilliseconds = Long.parseLong(timeString);
		return new Duration(totalMilliseconds);
	}
	
	public void setRpcTimeout(Duration timeout) {
		env.setHeader(EnvelopeHeaderConstants.MESSAGE_PATTERN_RPC_TIMEOUT, Long.toString(timeout.getMillis()));
	}
	
	
	public DateTime getCreationTime(Envelope envelope) {
        String createTicks = null;

        if (envelope.getHeaders().containsKey(EnvelopeHeaderConstants.ENVELOPE_CREATION_TIME)) {
            createTicks = envelope.getHeaders().get(EnvelopeHeaderConstants.ENVELOPE_CREATION_TIME);
        }

        return new DateTime(Long.parseLong(createTicks));
    }

    public void setCreationTime(Envelope envelope, DateTime date) {
        envelope.getHeaders().put(EnvelopeHeaderConstants.ENVELOPE_CREATION_TIME, Long.toString(date.getMillis()));
    }

    
    public DateTime getReceiptTime(Envelope envelope) {
        String receiptTicks = null;

        if (envelope.getHeaders().containsKey(EnvelopeHeaderConstants.ENVELOPE_RECEIPT_TIME)) {
            receiptTicks = envelope.getHeaders().get(EnvelopeHeaderConstants.ENVELOPE_RECEIPT_TIME);
        }

        return new DateTime(Long.parseLong(receiptTicks));
    }

    public void setReceiptTime(Envelope envelope, DateTime date) {
        envelope.getHeaders().put(EnvelopeHeaderConstants.ENVELOPE_RECEIPT_TIME, Long.toString(date.getMillis()));
    }
    
    
	public byte[] getDigitalSignature() {
		String base64String = env.getHeader(EnvelopeHeaderConstants.MESSAGE_SENDER_SIGNATURE);
		if (null == base64String) {
			return null;
		}
		
		return Base64.decodeBase64(base64String);
	}
	
	public void setDigitalSignature(byte[] signature) {
		env.setHeader(EnvelopeHeaderConstants.MESSAGE_SENDER_SIGNATURE, Base64.encodeBase64String(signature));
	}
	
	
	public boolean IsRpc() {
		return EnvelopeHeaderConstants.MESSAGE_PATTERN_RPC.equals(env.getHeader(EnvelopeHeaderConstants.MESSAGE_PATTERN));
	}
	
	public boolean IsPubSub() {
		return EnvelopeHeaderConstants.MESSAGE_PATTERN_PUBSUB.equals(env.getHeader(EnvelopeHeaderConstants.MESSAGE_PATTERN));
	}
	
	public boolean IsRequest() {
        // we assume that the envelope is holding a request if it is marked
        // as an rpc message that has no correlation id set.
        UUID correlationId = new EnvelopeHelper(env).getCorrelationId();

        return ( (new EnvelopeHelper(env).IsRpc()) && (null == correlationId) );
	}


	public String flatten() {
		return this.flatten(",");
	}
	
	public String flatten(String separator) {
		StringBuilder sb = new StringBuilder();
		
        sb.append("[");
        
        for (Entry<String, String> kvp : this.env.getHeaders().entrySet()) {
        	sb.append(String.format("%s{%s:%s}", separator, kvp.getKey(), kvp.getValue()));
        }

        if (sb.length()> 1)
        {
            sb.delete(1, separator.length());
        }

        sb.append("]");
        
		return sb.toString();
	}
}

package cmf.bus.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import cmf.bus.core.IEnvelope;

public class Envelope implements IEnvelope {

    private Map<String, String> headers = new HashMap<String, String>();
    private byte[] payload = {};

    public Envelope() {

    }

    public String getMessageId() {
        return getHeader(MESSAGE_ID);
    }

    public void setMessageId(String messageId) {
        if (StringUtils.isBlank(messageId)) {
            throw new IllegalArgumentException("Envelope message id cannot be set to null or an empty string.");
        }
        setHeader(MESSAGE_ID, messageId);
    }

    public String getMessageCorrelationId() {
        return getHeader(MESSAGE_CORRELATION_ID);
    }

    public void setMessageCorrelationId(String messageCorrelationId) {
        if (StringUtils.isBlank(messageCorrelationId)) {
            throw new IllegalArgumentException(
                            "Envelope message correlation id cannot be set to null or an empty string.");
        }
        setHeader(MESSAGE_CORRELATION_ID, messageCorrelationId);
    }

    public String getMessageType() {
        return getHeader(MESSAGE_TYPE);
    }

    public void setMessageType(String messageType) {
        if (StringUtils.isBlank(messageType)) {
            throw new IllegalArgumentException("Envelope message type cannot be set to null or an empty string.");
        }
        setHeader(MESSAGE_TYPE, messageType);
    }

    public String getTimestamp() {
        return getHeader(TIMESTAMP);
    }

    public void setTimestamp(String timestamp) {
        if (StringUtils.isBlank(timestamp)) {
            throw new IllegalArgumentException("Envelope timestamp cannot be set to null or an empty string.");
        }
        setHeader(TIMESTAMP, timestamp);
    }

    @Override
    public String getHeader(String key) {
        return headers.get(key);
    }

    @Override
    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public byte[] getPayload() {
        return payload;
    }

    @Override
    public void setPayload(byte[] payload) {
        if (ArrayUtils.isEmpty(payload)) {
            throw new IllegalArgumentException("Envelope payload cannot be set to null or an empty array.");
        }
        this.payload = payload;
    }

}

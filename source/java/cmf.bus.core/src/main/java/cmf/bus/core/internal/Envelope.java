package cmf.bus.core.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import cmf.bus.core.IEnvelope;

public class Envelope implements IEnvelope {

    protected Map<String, String> headers = new HashMap<String, String>();
    protected byte[] payload = {};

    @Override
    public String getCorrelationId() {
        return getHeader(CORRELATION_ID);
    }

    @Override
    public String getHeader(String key) {
        return headers.get(key);
    }

    @Override
    public String getId() {
        return getHeader(ID);
    }

    @Override
    public byte[] getPayload() {
        return payload;
    }

    @Override
    public String getReplyingTo() {
        return headers.get(REPLYING_TO);
    }

    @Override
    public String getTimestamp() {
        return getHeader(TIMESTAMP);
    }

    @Override
    public void setCorrelationId(String correlationId) {
        if (StringUtils.isBlank(correlationId)) {
            throw new IllegalArgumentException("Envelope correlation id cannot be set to null or an empty string");
        }
        setHeader(CORRELATION_ID, correlationId);
    }

    @Override
    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public void setId(String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("Envelope id cannot be set to null or an empty string");
        }
        setHeader(ID, id);
    }

    @Override
    public void setPayload(byte[] payload) {
        if (ArrayUtils.isEmpty(payload)) {
            throw new IllegalArgumentException("Envelope payload cannot be set to null or an empty array");
        }
        this.payload = payload;
    }

    @Override
    public void setReplyingTo(String replyingTo) {
        setHeader(REPLYING_TO, replyingTo);
    }

    @Override
    public void setTimestamp(String timestamp) {
        if (StringUtils.isBlank(timestamp)) {
            throw new IllegalArgumentException("Envelope timestamp cannot be set to null or an empty string");
        }
        setHeader(TIMESTAMP, timestamp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Entry<String, String> header : headers.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(String.format("\"%s\":\":%s\"", header.getKey(), header.getValue()));
        }
        sb.append("}");

        return sb.toString();
    }

}

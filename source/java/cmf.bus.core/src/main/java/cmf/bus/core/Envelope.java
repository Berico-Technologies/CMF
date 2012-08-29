package cmf.bus.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;


public class Envelope implements IEnvelope {

    protected Map<String, String> headers = new HashMap<String, String>();

    protected byte[] payload = {};

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Envelope)) {
            return false;
        }
        Envelope other = (Envelope) obj;
        if (!Arrays.equals(payload, other.payload)) {
            return false;
        } else if (!Arrays.equals(headers.values().toArray(), other.headers.values().toArray())) {
            return false;
        }

        return true;
    }

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
    public String getTimestamp() {
        return getHeader(TIMESTAMP);
    }

    public String getTopic() {
        return getHeader(TOPIC);
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
        if (payload == null) {
            throw new IllegalArgumentException("Envelope payload cannot be set to null");
        }
        this.payload = payload;
    }

    @Override
    public void setTimestamp(String timestamp) {
        if (StringUtils.isBlank(timestamp)) {
            throw new IllegalArgumentException("Envelope timestamp cannot be set to null or an empty string");
        }
        setHeader(TIMESTAMP, timestamp);
    }

    public void setTopic(String topic) {
        if (StringUtils.isBlank(topic)) {
            throw new IllegalArgumentException("Envelope topic cannot be set to null or an empty string");
        }
        setHeader(TOPIC, topic);
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

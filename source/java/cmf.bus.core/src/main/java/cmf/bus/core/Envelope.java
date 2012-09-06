package cmf.bus.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class Envelope {

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

    public String getHeader(String key) {
        return headers.get(key);
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setPayload(byte[] payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Envelope payload cannot be set to null");
        }
        this.payload = payload;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        String separator = "";
        for (Entry<String, String> header : headers.entrySet()) {
            sb.append(separator).append(String.format("\"%s\":\":%s\"", header.getKey(), header.getValue()));
            separator = ", ";
        }
        sb.append("}");

        return sb.toString();
    }
}

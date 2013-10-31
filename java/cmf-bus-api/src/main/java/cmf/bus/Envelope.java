package cmf.bus;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a discreet message sent across the bus.  Envelopes consist of two 
 * parts, payload represented as an array of bytes and optionally a set of headers 
 * represented as string/string map of key/value pairs.  Together they comprise 
 * the message and any appropriate meta-data pertaining to it respectively.
 * @see IEnvelopeSender#send(Envelope)
 * @see IRegistration#handle(Envelope)
 */
public class Envelope {

    protected Map<String, String> headers = new HashMap<String, String>();

    protected byte[] payload = {};

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

    /**
     * Gets the header value for the key specified.
     * @param key  The key that identifies the header value to return.
     * @return The value of the specified header.
     */
    public String getHeader(String key) {
        return headers.get(key);
    }

    /**
     * Gets all the envelope headers as a key-value map.  Caution: modifying the returned map
     * will modify the envelopes headers. 
     * @return A key/value map of headers.
     */
   public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Gets the payload (i.e. the actual content) of the envelope.
     * @return The content of the envelope as a byte array.
     */
    public byte[] getPayload() {
        return payload;
    }

    /**
     * Sets a header value for the specified key.
     * @param key The key that identifies the header value to set.
     * @param value The value to set the header to.
     */
    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Sets all headers based on the provided map.  Calling this method will delete
     * any pre-existing headers.
     * 
     * @param headers A key/value map of headers for the envelop.
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Sets the payload of the envelope.
     * 
     * @param payload A byte array containing the payload of the envelope.
     */
    public void setPayload(byte[] payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Envelope payload cannot be set to null");
        }
        this.payload = payload;
    }

    /**
     * Returns a string representation of the envelope that lists the key/value pairs 
     * contained in the envelope's headers.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        boolean first = true;

        for (Map.Entry<String, String> header : this.headers.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            else {
                first = false;
            }

            sb.append("\"");
            sb.append(header.getKey());
            sb.append("\" : \"");
            sb.append(header.getValue());
            sb.append("\"");
        }

        sb.append("}");
        return sb.toString();
    }
}

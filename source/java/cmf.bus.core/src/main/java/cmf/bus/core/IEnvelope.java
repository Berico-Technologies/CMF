package cmf.bus.core;

public interface IEnvelope {

    public static final String CORRELATION_ID = "bus.envelope.correlation_id";
    public static final String ID = "bus.envelope.id";
    public static final String REPLYING_TO = "bus.envelope.replying_to";
    public static final String TIMESTAMP = "bus.envelope.timestamp";

    String getCorrelationId();

    String getHeader(String key);

    String getId();

    byte[] getPayload();

    String getReplyingTo();

    String getTimestamp();

    void setCorrelationId(String correlationId);

    void setHeader(String key, String value);

    void setId(String id);

    void setPayload(byte[] payload);

    void setReplyingTo(String replyingTo);

    void setTimestamp(String timestamp);

}

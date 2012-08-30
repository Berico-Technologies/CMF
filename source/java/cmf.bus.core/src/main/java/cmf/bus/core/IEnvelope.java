package cmf.bus.core;

public interface IEnvelope {

    public static final String CORRELATION_ID = "bus.envelope.correlation_id";
    public static final String ID = "bus.envelope.id";
    public static final String TOPIC = "bus.envelope.topic";
    public static final String TIMESTAMP = "bus.envelope.timestamp";

    String getCorrelationId();

    String getHeader(String key);

    String getId();

    byte[] getPayload();

    String getTimestamp();

    void setCorrelationId(String correlationId);

    void setHeader(String key, String value);

    void setId(String id);

    void setPayload(byte[] payload);

    void setTimestamp(String timestamp);
    
    String getTopic();
    
    void setTopic(String topic);

}

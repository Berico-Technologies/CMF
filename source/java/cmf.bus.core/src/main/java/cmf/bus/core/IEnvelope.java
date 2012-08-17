package cmf.bus.core;

public interface IEnvelope {

    public static final String MESSAGE_ID = "cmf.bus.message_id";
    public static final String MESSAGE_CORRELATION_ID = "cmf.bus.message_correlation_id";
    public static final String MESSAGE_TYPE = "cmf.bus.message_type";
    public static final String TIMESTAMP = "cmf.bus.timestamp";

    String getHeader(String key);

    void setHeader(String key, String value);

    byte[] getPayload();

    void setPayload(byte[] payload);

}

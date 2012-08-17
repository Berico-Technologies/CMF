package cmf.bus.core;

public interface IEnvelope {

    String getHeader(String key);

    void setHeader(String key, String value);

    byte[] getPayload();

    void setPayload(byte[] payload);

}

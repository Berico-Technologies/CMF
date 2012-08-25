package cmf.bus.core.serialize;

public interface ISerializer {

    public static final String ENCODING = "UTF-8";

    String stringSerialize(Object deserialized);

    <TYPE> TYPE stringDeserialize(String serialized, Class<TYPE> type);

    byte[] byteSerialize(Object deserialized);

    <TYPE> TYPE byteDeserialize(byte[] serialized, Class<TYPE> type);

}

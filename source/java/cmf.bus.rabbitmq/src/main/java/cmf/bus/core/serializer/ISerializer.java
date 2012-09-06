package cmf.bus.core.serializer;

public interface ISerializer {

    public static final String ENCODING = "UTF-8";

    <TYPE> TYPE byteDeserialize(byte[] serialized, Class<TYPE> type);

    byte[] byteSerialize(Object deserialized);

    <TYPE> TYPE stringDeserialize(String serialized, Class<TYPE> type);

    String stringSerialize(Object deserialized);

}

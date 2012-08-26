package cmf.bus.core.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSerializer implements ISerializer {

    private Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

    @Override
    public <TYPE> TYPE byteDeserialize(byte[] serialized, Class<TYPE> type) {
        try {
            return stringDeserialize(new String(serialized, ENCODING), type);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error encoding bytes", e);
        }
    }

    @Override
    public byte[] byteSerialize(Object deserialized) {
        try {
            return stringSerialize(deserialized).getBytes(ENCODING);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error encoding bytes", e);
        }
    }

    @Override
    public <TYPE> TYPE stringDeserialize(String serialized, Class<TYPE> type) {
        TYPE object = null;
        try {
            object = gson.fromJson(serialized, type);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing object.", e);
        }

        return object;
    }

    @Override
    public String stringSerialize(Object deserialized) {
        String json = null;
        try {
            json = gson.toJson(deserialized);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing object.", e);
        }

        return json;
    }

}

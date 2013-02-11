package cmf.eventing.berico;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSerializer implements ISerializer {

    private static final Logger log = LoggerFactory.getLogger(GsonSerializer.class);
    
    private class DotNetTypeExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipClass(Class<?> arg0) {
            return false; // don't skip any classes
        }

        @Override
        public boolean shouldSkipField(FieldAttributes arg0) {
            return "$type".equals(arg0.getName()); // ignore fields named $type which may be added by the dotnet serializer
        }
        
    }

    private class GsonIgnoreExclusionStrategy implements ExclusionStrategy {
        private final Class<?> typeToSkip;

        private GsonIgnoreExclusionStrategy(Class<?> typeToSkip) {
            this.typeToSkip = typeToSkip;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(GsonIgnore.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return (clazz == typeToSkip);
        }
    }
    
    protected Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .setExclusionStrategies(
                    new DotNetTypeExclusionStrategy(),
                    new GsonIgnoreExclusionStrategy(GsonIgnore.class)
            ).create();

    @Override
    public <TYPE> TYPE byteDeserialize(byte[] serialized, Class<TYPE> type) {
        try {
            String json = new String(serialized, ENCODING);
            log.debug("Will attempt to deserialize: " + json);

            return stringDeserialize(json, type);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error encoding bytes", e);
        }
    }

    @Override
    public byte[] byteSerialize(Object deserialized) {
        try {
            String json = stringSerialize(deserialized);
            log.debug("Serialized event: " + json);

            return json.getBytes(ENCODING);
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

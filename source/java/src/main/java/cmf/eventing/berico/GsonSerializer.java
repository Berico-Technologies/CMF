package cmf.eventing.berico;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSerializer implements ISerializer {

    protected Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
    protected Logger log;
    
    
    public GsonSerializer() {
    	this.log = LoggerFactory.getLogger(this.getClass());
    }

    
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
        	String json = this.stringSerialize(deserialized);
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

package cmf.eventing.berico;

import java.util.HashMap;
import java.util.Map;

import cmf.bus.Envelope;

public class ProcessingContext {

    protected Envelope envelope;
    protected Object event;
    protected Map<String, Object> properties;

    public ProcessingContext(Envelope env, Object event) {
        envelope = env;
        this.event = event;
        properties = new HashMap<String, Object>();
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public Object getEvent() {
        return event;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public void setEnvelope(Envelope env) {
        envelope = env;
    }

    public void setEvent(Object event) {
        this.event = event;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }
}

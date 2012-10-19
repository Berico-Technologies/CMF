package cmf.eventing.berico;

import java.util.HashMap;
import java.util.Map;

import cmf.bus.Envelope;

public class ProcessingContext {

	protected Envelope envelope;
	protected Object event;
	protected Map<String, Object> properties;
	
	
	public Envelope getEnvelope() { return this.envelope; }
	public void setEnvelope(Envelope env) { this.envelope = env; }
	
	public Object getEvent() { return this.event; }
	public void setEvent(Object event) { this.event = event; }
	
	public Map<String, Object> getProperties() { return this.properties; }
	public void setProperties(Map<String, Object> properties) { this.properties = properties; }
	
	
	public ProcessingContext(Envelope env, Object event) {
		this.envelope = env;
		this.event = event;
		this.properties = new HashMap<String, Object>();
	}
	
	

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, Object value) {
    	properties.put(key, value);
    }
}

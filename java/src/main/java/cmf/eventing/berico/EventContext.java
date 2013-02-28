package cmf.eventing.berico;

import java.util.HashMap;
import java.util.Map;

import cmf.bus.Envelope;

public class EventContext {

	public enum Directions { In, Out }
	
	
	private Directions _direction;
	private Object _event;
	private Envelope _envelope;
	private Map<String, Object> _properties;
	
	
	public Directions getDirection() { return _direction; }
	public void setDirection(Directions value) { _direction = value; }
	
	public Envelope getEnvelope() { return _envelope; }
	public void setEnvelope(Envelope value) { _envelope = value; }
	
	public Map<String, Object> getProperties() { return _properties; }
	public void setProperties(Map<String, Object> value) { _properties = value; }
	
	public Object getEvent() { return _event; }
	public void setEvent(Object value) { _event = value; }
	
	
	public EventContext(Directions direction) {
		_properties = new HashMap<String, Object>();
		
		_direction = direction;
	}
	
	public EventContext(Directions direction, Envelope envelope) {
		this(direction);
		_envelope = envelope;
	}
	
	public EventContext(Directions direction, Envelope envelope, Object event) {
		this(direction, envelope);
		_event = event;
	}
	
	
	public Object getProperty(String name) {
		if (_properties.containsKey(name)) {
			return _properties.get(name);
		}
		else {
			return null;
		}
	}
	
	public void setProperty(String name, Object value) {
		_properties.put(name, value);
	}
}

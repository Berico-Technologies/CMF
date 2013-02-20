package cmf.bus.berico;

import java.util.HashMap;
import java.util.Map;

import cmf.bus.Envelope;

public class EnvelopeContext {

	public enum Directions { In, Out }
	
	
	protected Envelope _envelope;
	protected Map<String, Object> _properties;
	protected Directions _direction;
	
	
	public Envelope getEnvelope() { return _envelope; }
	public void setEnvelope(Envelope value) { _envelope = value; }
	
	public Directions getDirection() { return _direction; }
	public void setDirection(Directions value) { _direction = value; }
	
	public Map<String, Object> getProperties() { return _properties; }
	
	public Object getProperty(String key) {
		Object value = null;
		
		if ( _properties.containsKey(key)) {
			value = _properties.get(key);
		}
		
		return value;
	}
	public void setProperty(String key, Object value) {
		_properties.put(key, value);
	}

	
	public EnvelopeContext(Directions direction) {
		_direction = direction;
		_properties = new HashMap<String, Object>();
	}
	
	public EnvelopeContext(Directions direction, Envelope envelope) {
		this(direction);
		_envelope = envelope;
	}
}

package cmf.bus.berico;

import java.util.Map.Entry;

import cmf.bus.Envelope;

public class EnvelopeHelper {

	private Envelope env;
	
	
	public EnvelopeHelper(Envelope envelope) {
		this.env = envelope;
	}
	
	
	public String flatten() {
		return this.flatten(",");
	}
	
	public String flatten(String separator) {
		StringBuilder sb = new StringBuilder();
		
        sb.append("[");
        
        for (Entry<String, String> kvp : this.env.getHeaders().entrySet()) {
        	sb.append(String.format("%s{%s:%s}", separator, kvp.getKey(), kvp.getValue()));
        }

        if (sb.length()> 1)
        {
            sb.delete(1, separator.length());
        }

        sb.append("]");
        
		return sb.toString();
	}
}

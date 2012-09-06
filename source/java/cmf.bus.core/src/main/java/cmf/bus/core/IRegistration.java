package cmf.bus.core;

import java.util.Map;

public interface IRegistration {

    Object handle(Envelope envelope);

    Object handleFailed(Envelope envelope, Exception e);
    
    boolean filter(Envelope envelope);
    
    Map<String, String> info();
}

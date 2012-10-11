package cmf.bus.eventing.rpc;

import java.util.Map;

public interface IRpcSender {

    void respondTo(Map<String, String> originalHeaders, Object response);
}

package cmf.eventing.patterns.rpc;

import java.util.Map;

public interface IRpcSender {

    void respondTo(Map<String, String> originalHeaders, Object response);
}

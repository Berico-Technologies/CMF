package cmf.bus.berico.rabbit;

import java.util.List;
import java.util.Map;

public class TopologyRegistry {

    private Map<String, List<Route>> receiveRouteMap;
    private Map<String, List<Route>> sendRouteMap;

    public List<Route> getReceiveRoutes(String key) {
        return receiveRouteMap.get(key);
    }

    public List<Route> getSendRoutes(String key) {
        return sendRouteMap.get(key);
    }

    public void setReceiveRouteMap(Map<String, List<Route>> receiveRouteMap) {
        this.receiveRouteMap = receiveRouteMap;
    }

    public void setSendRouteMap(Map<String, List<Route>> sendRouteMap) {
        this.sendRouteMap = sendRouteMap;
    }
}

package cmf.bus.pubsub.transport;

import java.util.Collection;
import java.util.Map;

public class TopologyRegistry {

    private Map<String, Collection<Route>> sendRouteMap;
    private Map<String, Collection<Route>> receiveRouteMap;

    public Collection<Route> getSendRoutes(String key) {
        return sendRouteMap.get(key);
    }

    public void setSendRouteMap(Map<String, Collection<Route>> sendRouteMap) {
        this.sendRouteMap = sendRouteMap;
    }

    public Collection<Route> getReceiveRoutes(String key) {
        return receiveRouteMap.get(key);
    }

    public void setReceiveRouteMap(Map<String, Collection<Route>> receiveRouteMap) {
        this.receiveRouteMap = receiveRouteMap;
    }

}

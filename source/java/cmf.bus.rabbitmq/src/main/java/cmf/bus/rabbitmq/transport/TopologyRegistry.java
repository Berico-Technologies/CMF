package cmf.bus.rabbitmq.transport;

import java.util.Collection;
import java.util.Map;

public class TopologyRegistry {

    private Map<String, Collection<Route>> receiveRouteMap;
    private Map<String, Collection<Route>> sendRouteMap;

    public Collection<Route> getReceiveRouteCollection(String key) {
        return receiveRouteMap.get(key);
    }

    public Collection<Route> getSendRouteCollection(String key) {
        return sendRouteMap.get(key);
    }

    public void setReceiveRouteMap(Map<String, Collection<Route>> receiveRouteMap) {
        this.receiveRouteMap = receiveRouteMap;
    }

    public void setSendRouteMap(Map<String, Collection<Route>> sendRouteMap) {
        this.sendRouteMap = sendRouteMap;
    }

}

package cmf.rabbit;

import java.util.List;
import java.util.Map;

public class TopologyRegistry implements ITopologyRegistry {

    private Map<String, List<Route>> receiveRouteMap;
    private Map<String, List<Route>> sendRouteMap;

    @Override
    public List<Route> getReceiveRoutes(String key) {
        return receiveRouteMap.get(key);
    }

    @Override
    public List<Route> getSendRoutes(String key) {
        return sendRouteMap.get(key);
    }

    @Override
    public void setReceiveRouteMap(Map<String, List<Route>> receiveRouteMap) {
        this.receiveRouteMap = receiveRouteMap;
    }

    @Override
    public void setSendRouteMap(Map<String, List<Route>> sendRouteMap) {
        this.sendRouteMap = sendRouteMap;
    }
}

package cmf.rabbit;

import java.util.List;
import java.util.Map;

public interface ITopologyRegistry {

    List<Route> getReceiveRoutes(String key);

    List<Route> getSendRoutes(String key);

    void setReceiveRouteMap(Map<String, List<Route>> receiveRouteMap);

    void setSendRouteMap(Map<String, List<Route>> sendRouteMap);
}

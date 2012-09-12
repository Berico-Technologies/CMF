package cmf.bus.berico.rabbit;

import java.util.List;

public interface ITopologyProvider {

    List<Route> getReceiveRoutes(String routingKey);

    List<Route> getSendRoutes(String routingKey);
}

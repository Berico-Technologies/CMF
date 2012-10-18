package cmf.rabbit.topology;

import java.util.Map;

public interface ITopologyService {
	RoutingInfo getRoutingInfo(Map<String, String> routingHints);
}

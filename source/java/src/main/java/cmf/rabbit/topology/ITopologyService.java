package cmf.rabbit.topology;

import java.util.List;
import java.util.Map;

public interface ITopologyService {

	RoutingInfo getRoutingInfo(Map<String, String> routingHints);
	
}

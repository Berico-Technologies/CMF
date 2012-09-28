package cmf.bus.berico.rabbit.support;

import java.util.LinkedList;
import java.util.List;

import cmf.bus.berico.rabbit.ITopologyProvider;
import cmf.bus.berico.rabbit.Route;

public class InMemoryTopologyProvider implements ITopologyProvider {

    private InMemoryTopologyRegistry topologyRegistry;

    public InMemoryTopologyProvider(String profile, InMemoryTopologyRegistry topologyRegistry) {
        this.topologyRegistry = topologyRegistry;
    }

    @Override
    public List<Route> getReceiveRoutes(String routingKey) {
        List<Route> receiveRoutes = new LinkedList<Route>();
        for (Route route : topologyRegistry.getReceiveRoutes(routingKey)) {
            receiveRoutes.add(route.getCopy());
        }

        return receiveRoutes;
    }

    @Override
    public List<Route> getSendRoutes(String routingKey) {
        List<Route> sendRoutes = new LinkedList<Route>();
        for (Route route : topologyRegistry.getSendRoutes(routingKey)) {
            sendRoutes.add(route.getCopy());
        }

        return sendRoutes;
    }
}

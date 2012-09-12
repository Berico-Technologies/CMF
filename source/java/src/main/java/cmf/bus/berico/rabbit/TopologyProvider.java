package cmf.bus.berico.rabbit;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.eventing.IEventBus;
import cmf.bus.eventing.IEventHandler;

public class TopologyProvider implements ITopologyProvider {

    @SuppressWarnings("unused")
    private String profile;
    private TopologyRegistry topologyRegistry;

    public TopologyProvider(String profile, TopologyRegistry topologyRegistry) {
        this.profile = profile;
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

    public void setEventBus(IEventBus eventBus) {
        eventBus.subscribe(new IEventHandler<TopologyUpdateResponse>() {

            @Override
            public Class<TopologyUpdateResponse> getEventType() {
                return TopologyUpdateResponse.class;
            }

            @Override
            public Object handle(TopologyUpdateResponse event, Map<String, String> headers) {
                topologyRegistry = event.getTopologyRegistry();

                return null;
            }

            @Override
            public Object handleFailed(Envelope envelope, Exception e) {
                return null;
            }
        });
    }
}

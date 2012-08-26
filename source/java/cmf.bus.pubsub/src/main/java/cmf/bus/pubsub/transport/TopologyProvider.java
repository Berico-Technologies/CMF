package cmf.bus.pubsub.transport;

import java.util.Collection;
import java.util.LinkedList;

import cmf.bus.core.DeliveryOutcome;
import cmf.bus.core.event.IEventHandler;
import cmf.bus.pubsub.Envelope;
import cmf.bus.pubsub.Registration;
import cmf.bus.pubsub.event.EventBus;

public class TopologyProvider {

    public class TopologyUpdateResponseHandler implements IEventHandler<TopologyUpdateResponse> {

        @Override
        public DeliveryOutcome receive(TopologyUpdateResponse topologyUpdateResponse) {
            setTopologyRegistry(topologyUpdateResponse.getTopologyRegistry());

            return DeliveryOutcome.Acknowledge;
        }

    }

    private String profile;

    private TopologyRegistry topologyRegistry;

    public String getProfile() {
        return profile;
    }

    public Collection<Route> getReceiveRoutes(Registration registration) {
        Collection<Route> receiveRoutes = null;
        try {
            receiveRoutes = topologyRegistry.getReceiveRoutes(registration.getTopic());
        } catch (Exception e) {
            receiveRoutes = new LinkedList<Route>();
        }

        return receiveRoutes;
    }

    public Collection<Route> getSendRoutes(Envelope envelope) {
        Collection<Route> sendRoutes = null;
        try {
            sendRoutes = topologyRegistry.getReceiveRoutes(envelope.getTopic());
        } catch (Exception e) {
            sendRoutes = new LinkedList<Route>();
        }

        return sendRoutes;
    }

    public void setEventBus(EventBus eventBus) {
        eventBus.register(new TopologyUpdateResponseHandler(), TopologyUpdateResponse.class);
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setTopologyRegistry(TopologyRegistry topologyRegistry) {
        this.topologyRegistry = topologyRegistry;
    }

}

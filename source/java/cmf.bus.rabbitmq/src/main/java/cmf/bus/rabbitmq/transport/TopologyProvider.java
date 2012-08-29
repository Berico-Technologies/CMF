package cmf.bus.rabbitmq.transport;

import java.util.Collection;
import java.util.LinkedList;

import cmf.bus.core.DeliveryOutcome;
import cmf.bus.core.Envelope;
import cmf.bus.core.Registration;
import cmf.bus.core.event.EventBus;
import cmf.bus.core.event.IEventHandler;

public class TopologyProvider {

    public class TopologyUpdateResponseHandler implements IEventHandler<TopologyUpdateResponse> {

        @Override
        public DeliveryOutcome handleEvent(TopologyUpdateResponse topologyUpdateResponse) {
            setTopologyRegistry(topologyUpdateResponse.getTopologyRegistry());

            return DeliveryOutcome.Acknowledge;
        }

    }

    private String profile;

    private TopologyRegistry topologyRegistry;

    public String getProfile() {
        return profile;
    }

    public Collection<Route> getReceiveRouteCollection(Registration registration) {
        Collection<Route> receiveRouteCollection = null;
        try {
            receiveRouteCollection = topologyRegistry.getReceiveRouteCollection(registration.getTopic());
        } catch (Exception e) {
            receiveRouteCollection = new LinkedList<Route>();
        }

        return receiveRouteCollection;
    }

    public Collection<Route> getSendRouteCollection(Envelope envelope) {
        Collection<Route> sendRouteCollection = null;
        try {
            sendRouteCollection = topologyRegistry.getSendRouteCollection(envelope.getTopic());
        } catch (Exception e) {
            sendRouteCollection = new LinkedList<Route>();
        }

        return sendRouteCollection;
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

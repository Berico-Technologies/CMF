package cmf.bus.pubsub.transport;

import java.util.Collection;

import cmf.bus.pubsub.Envelope;
import cmf.bus.pubsub.Registration;

public interface IBroker {

    public static final int DEFAULT_QUEUE_LIFETIME = 1000 * 60 * 30; // 30 minute lifetime

    void register(Registration registration, Collection<Route> routes);

    void send(Envelope envelope, Collection<Route> routes);

    void setQueueLifetime(int queueLifetime);

}

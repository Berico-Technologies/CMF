package cmf.bus.pubsub;

import cmf.bus.core.IEnvelopeHandler;

public class EnvelopeBus extends cmf.bus.core.internal.EnvelopeBus {

    public Registration register(String topic, IEnvelopeHandler envelopeHandler) {
        Registration registration = new Registration();
        registration.setTopic(topic);
        registration.setEnvelopeHandler(envelopeHandler);
        register(registration);

        return registration;
    }

}

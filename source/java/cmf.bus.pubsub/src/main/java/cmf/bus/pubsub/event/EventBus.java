package cmf.bus.pubsub.event;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import cmf.bus.core.DeliveryOutcome;
import cmf.bus.core.IEnvelope;
import cmf.bus.core.IEnvelopeHandler;
import cmf.bus.core.event.IEventBus;
import cmf.bus.core.event.IEventHandler;
import cmf.bus.core.serializer.ISerializer;
import cmf.bus.pubsub.Envelope;
import cmf.bus.pubsub.EnvelopeBus;

public class EventBus implements IEventBus {

    protected EnvelopeBus envelopeBus;
    protected ISerializer serializer;

    @Override
    public <EVENT> void register(final IEventHandler<EVENT> eventHandler, final Class<EVENT> type) {
        String topic = type.getCanonicalName();
        IEnvelopeHandler envelopeHandler = new IEnvelopeHandler() {

            @Override
            public DeliveryOutcome receive(IEnvelope envelope) throws Exception {
                EVENT event = serializer.byteDeserialize(envelope.getPayload(), type);

                return eventHandler.receive(event);
            }

        };
        envelopeBus.register(topic, envelopeHandler);
    }

    @Override
    public void send(Object event) {
        String timestamp = DateTime.now(DateTimeZone.UTC).toString();
        byte[] payload = {};
        try {
            payload = serializer.byteSerialize(event);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing event", e);
        }
        String topic = event.getClass().getCanonicalName();
        Envelope envelope = new Envelope();
        envelope.setPayload(payload);
        envelope.setTimestamp(timestamp);
        envelope.setTopic(topic);
        envelopeBus.send(envelope);
    }

    public void setEnvelopeBus(EnvelopeBus envelopeBus) {
        this.envelopeBus = envelopeBus;
    }

    public void setSerializer(ISerializer serializer) {
        this.serializer = serializer;
    }

}

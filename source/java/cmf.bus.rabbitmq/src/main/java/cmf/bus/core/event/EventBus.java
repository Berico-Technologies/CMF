package cmf.bus.core.event;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import cmf.bus.core.DeliveryOutcome;
import cmf.bus.core.Envelope;
import cmf.bus.core.Envelope;
import cmf.bus.core.IEnvelopeBus;
import cmf.bus.core.IEnvelopeHandler;
import cmf.bus.core.Registration;
import cmf.bus.core.serializer.ISerializer;

public class EventBus implements IEventBus {

    protected IEnvelopeBus envelopeBus;
    protected ISerializer serializer;

    @Override
    public <EVENT> void register(final IEventHandler<EVENT> eventHandler, final Class<EVENT> type) {
        String topic = type.getCanonicalName();
        IEnvelopeHandler envelopeHandler = new IEnvelopeHandler() {

            @Override
            public DeliveryOutcome handleEnvelope(Envelope envelope) {
                EVENT event = serializer.byteDeserialize(envelope.getPayload(), type);

                return eventHandler.handleEvent(event);
            }

        };
        Registration registration = new Registration();
        registration.setTopic(topic);
        registration.setEnvelopeHandler(envelopeHandler);
        envelopeBus.register(registration);
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

    public void setEnvelopeBus(IEnvelopeBus envelopeBus) {
        this.envelopeBus = envelopeBus;
    }

    public void setSerializer(ISerializer serializer) {
        this.serializer = serializer;
    }

}

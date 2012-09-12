package cmf.bus.eventing.berico;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import cmf.bus.Envelope;
import cmf.bus.berico.rabbit.EnvelopeConstants;
import cmf.bus.eventing.ISerializer;

public class EnvelopeHelper {

    public static <TEVENT> TEVENT unwrapEvent(Envelope envelope, ISerializer serializer) {
        TEVENT event = null;
        try {
            byte[] serializedEvent = envelope.getPayload();
            String typeName = envelope.getHeader(EnvelopeConstants.TYPE);
            @SuppressWarnings("unchecked")
            Class<TEVENT> type = (Class<TEVENT>) Class.forName(typeName);
            event = serializer.byteDeserialize(serializedEvent, type);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing event");
        }

        return event;
    }

    public static Envelope wrapEvent(Object event, ISerializer serializer) {
        return wrapEvent(event, serializer, new HashMap<String, String>());
    }

    public static Envelope wrapEvent(Object event, ISerializer serializer, Map<String, String> headers) {
        Envelope envelope = new Envelope();
        envelope.setHeaders(headers);
        envelope.setPayload(serializer.byteSerialize(event));
        if (StringUtils.isBlank(envelope.getHeader(EnvelopeConstants.ID))) {
            envelope.setHeader(EnvelopeConstants.ID, UUID.randomUUID().toString());
        }
        envelope.setHeader(EnvelopeConstants.TYPE, event.getClass().getCanonicalName());
        if (StringUtils.isBlank(envelope.getHeader(EnvelopeConstants.TIMESTAMP))) {
            envelope.setHeader(EnvelopeConstants.TIMESTAMP, Long.toString(DateTime.now().getMillis()));
        }

        return envelope;
    }
}

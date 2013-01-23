package cmf.rabbit.support;

import java.util.Map;

import org.joda.time.DateTime;

import cmf.bus.Envelope;

public class RabbitEnvelopeHelper {

    public static class Headers {

        public static final String CORRELATION_ID = "cmf.bus.message.correlation_id";
        public static final String ID = "cmf.bus.message.id";
        public static final String TIMESTAMP = "cmf.bus.message.timestamp";
        public static final String TYPE = "cmf.bus.message.type";

        public static String getCorrelationId(Envelope envelope) {
            return envelope.getHeader(CORRELATION_ID);
        }

        public static String getCorrelationId(Map<String, String> headers) {
            return headers.get(CORRELATION_ID);
        }

        public static String getId(Envelope envelope) {
            return envelope.getHeader(ID);
        }

        public static String getId(Map<String, String> headers) {
            return headers.get(ID);
        }

        public static String getTimestamp(Envelope envelope) {
            return envelope.getHeader(TIMESTAMP);
        }

        public static String getTimestamp(Map<String, String> headers) {
            return headers.get(TIMESTAMP);
        }

        public static String getType(Envelope envelope) {
            return envelope.getHeader(TYPE);
        }

        public static String getType(Map<String, String> headers) {
            return headers.get(TYPE);
        }

        public static void setCorrelationId(Envelope envelope, String correlationId) {
            envelope.setHeader(CORRELATION_ID, correlationId);
        }

        public static void setCorrelationId(Map<String, String> headers, String correlationId) {
            headers.put(CORRELATION_ID, correlationId);
        }

        public static void setId(Envelope envelope, String id) {
            envelope.setHeader(ID, id);
        }

        public static void setId(Map<String, String> headers, String id) {
            headers.put(ID, id);
        }

        public static void setTimestamp(Envelope envelope, DateTime timestamp) {
            envelope.setHeader(TIMESTAMP, Long.toString(timestamp.getMillis()));
        }

        public static void setTimestamp(Envelope envelope, Long timestamp) {
            envelope.setHeader(TIMESTAMP, Long.toString(timestamp));
        }

        public static void setTimestamp(Envelope envelope, String timestamp) {
            envelope.setHeader(TIMESTAMP, timestamp);
        }

        public static void setTimestamp(Map<String, String> headers, DateTime timestamp) {
            headers.put(TIMESTAMP, Long.toString(timestamp.getMillis()));
        }

        public static void setTimestamp(Map<String, String> headers, Long timestamp) {
            headers.put(TIMESTAMP, Long.toString(timestamp));
        }

        public static void setTimestamp(Map<String, String> headers, String timestamp) {
            headers.put(TIMESTAMP, timestamp);
        }

        public static void setType(Envelope envelope, Object item) {
            envelope.setHeader(TYPE, item.getClass().getCanonicalName());
        }

        public static void setType(Envelope envelope, String type) {
            envelope.setHeader(TYPE, type);
        }

        public static void setType(Map<String, String> headers, Object item) {
            headers.put(TYPE, item.getClass().getCanonicalName());
        }

        public static void setType(Map<String, String> headers, String type) {
            headers.put(TYPE, type);
        }
    }
}

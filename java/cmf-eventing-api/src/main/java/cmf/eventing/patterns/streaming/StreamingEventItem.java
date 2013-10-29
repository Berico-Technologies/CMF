package cmf.eventing.patterns.streaming;

import java.util.Map;
import java.util.UUID;

import static cmf.eventing.patterns.streaming.StreamingEnvelopeConstants.*;

/**
 * This is a wrapper class that wraps each event received as part of an event stream
 * In addition to providing access to the wrapped event, it also provides access to 
 * additional stream specific meta data. 
 */
public class StreamingEventItem<TEVENT>  {
    private final TEVENT event;
    private final Map<String, String> eventHeaders;

    public StreamingEventItem(TEVENT event, Map<String, String> eventHeaders) {
        this.event = event;
        this.eventHeaders = eventHeaders;
    }

    /**
     * Gets the event received from the published stream
     * @return The received event.
     */
    public TEVENT getEvent() {
        return event;
    }

    /**
     * Gets the sequenceId value which is common to all events in the same stream.
     * @return
     */
    public UUID getSequenceId() {
        return UUID.fromString(eventHeaders.get(SEQUENCE_ID));
    }

    /**
     * Gets the ordinal position of this event within the event stream.
     * @return
     */
    public int getPosition() {
        return Integer.parseInt(eventHeaders.get(POSITION));
    }

    /**
     * Returns all headers associated with the received event.
     * @return
     */
    public Map<String, String> getEventHeaders() {
        return eventHeaders;
    }
}
package cmf.eventing.patterns.streaming;

import java.util.Map;
import java.util.UUID;

import static cmf.eventing.patterns.streaming.StreamingEnvelopeConstants.*;

/**
 * This is a wrapper class that wraps each event received as part of an event stream
 * In addition to providing access to the wrapped event, it also provides access to 
 * additional stream specific meta data. 
 * 
 * WARNING: The streaming event API and its accompanying implementation is deemed 
 * to be a proof of concept at this point and subject to change.  It should not be used 
 * in a production environment. 
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
     */
    public UUID getSequenceId() {
        return UUID.fromString(eventHeaders.get(SEQUENCE_ID));
    }

    /**
     * Gets the ordinal position of this event within the event stream.
     */
    public int getPosition() {
        return Integer.parseInt(eventHeaders.get(POSITION));
    }

    /**
     * Returns all headers associated with the received event.
     */
    public Map<String, String> getEventHeaders() {
        return eventHeaders;
    }
}
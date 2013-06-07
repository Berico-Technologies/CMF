package cmf.eventing.patterns.streaming;

import java.util.Map;
import java.util.UUID;

/**
 * Contains the elements needed to receive an event from an event stream.
 * This allows each discrete event pulled from a subscribed event stream to
 * contain the necessary state that a subscriber would need including the
 * sequenceId, position, and whether this event is the last in the published
 * sequence.
 * User: jholmberg
 * Date: 6/7/13
 */
public interface IStreamingEventItem<TEVENT> {
    /**
     * Get the event received from the published stream
     * @return
     */
    public TEVENT getEvent();

    /**
     * Helper method that extracts the unique sequenceId from the event headers
     * @return
     */
    public UUID getSequenceId();

    /**
     * Helper method that returns the position of this event in the event sequence
     * @return
     */
    public int getPosition();

    /**
     * Helper method that returns whether this event is the last in the sequence
     * @return
     */
    public boolean isLast();

    /**
     * Returns all headers associated with the event
     * @return
     */
    public Map<String, String> getEventHeaders();
}

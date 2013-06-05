package cmf.eventing;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Adds behavior to the {@link IEventHandler} allowing it to process
 * a stream of events sent via the {@link IStreamingEventBus}
 * User: jholmberg
 * Date: 6/4/13
 */
public interface IStreamingEventHandler<TEVENT> extends IEventHandler {
    /**
     * Aggregates all events of type TEVENT and stores them into a {@link java.util.Collection}
     * when the last event was received with the message header "isLast" set to true.
     * <p>
     *     This provides a way to gather all events belonging to a particular sequence and deliver them
     *     and a cohesive collection.
     * </p>
     * <p>
     *     A drawback, however, is that because it's holding the collection until all events in the stream
     *     have been received, it introduces a higher degree of latency into the process.
     *     This can be especially apparent the larger the sequence becomes.
     * </p>
     * @param event
     * @param headers
     * @return
     */
    Collection<TEVENT> handleToCollection(TEVENT event, Map<String, String> headers);

    /**
     * Streams all events of type TEVENT as they are received from the bus. and places them into an
     * {@link java.util.Iterator} that can be returned immediately to the caller.
     * <p>
     *     The iterator will remain open until the last event is received with a message header "isLast" set to true.
     * </p>
     * <p>
     *     This implementation reduces the latency that the collection based option has but requires
     *     a little more complex code to handle the results as they are received.
     * </p>
     * @param event
     * @param headers
     * @return
     */
    Iterator<TEVENT> handleToStream(TEVENT event, Map<String, String> headers);
}

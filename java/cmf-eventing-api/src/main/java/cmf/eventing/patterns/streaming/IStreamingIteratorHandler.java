package cmf.eventing.patterns.streaming;

import cmf.eventing.IEventHandler;

import java.util.Iterator;
import java.util.Map;

/**
 * Adds behavior to the {@link cmf.eventing.IEventHandler} allowing it to process
 * a stream of events sent via the {@link IStreamingEventBus}
 * User: jholmberg
 * Date: 6/5/13
 */
public interface IStreamingIteratorHandler<TEVENT> extends IEventHandler {
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
     * @param eventStream
     * @param headers
     * @return
     */
    Object handleStream(Iterator<IStreamingEventItem<TEVENT>> eventStream, Map<String, String> headers);
}

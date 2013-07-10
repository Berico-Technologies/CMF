package cmf.eventing.patterns.streaming;

import cmf.bus.IDisposable;

/**
 * Adds behavior to the {@link cmf.eventing.IEventHandler} allowing it to process
 * a stream of events sent via the {@link IStreamingEventBus}
 * User: jholmberg
 * Date: 6/5/13
 */
public interface IStreamingReaderHandler<TEVENT> extends IDisposable {
    /**
     * Streams all events of type TEVENT as they are received from the bus and places them into an
     * {@link StreamingEventItem} that can be returned immediately to the caller.
     * <p>
     *     This method will continue to be called until the last message is handled at which point
     *     the {@link cmf.eventing.patterns.streaming.IStreamingReaderHandler#dispose()} is called to
     *     clean up any resources associated with the stream.
     * </p>
     * <p>
     *     This implementation reduces the latency that the collection based option has but requires
     *     a little more code complexity to handle the results as they are received.
     * </p>
     * @param eventItem
     */
    void onEventRead(StreamingEventItem<TEVENT> eventItem);

    Class<TEVENT> getEventType();
}

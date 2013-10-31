package cmf.eventing.patterns.streaming;

import cmf.bus.IDisposable;

/**
 * Defines an interface to be implemented by types that wish to process individual events
 * from a stream as they are received with minimal latency. 
 * 
 * WARNING: The streaming event API and its accompanying implementation is deemed 
 * to be a proof of concept at this point and subject to change.  It should not be used 
 * in a production environment. 
 * @see IStreamingEventConsumer#subscribeToReader(IStreamingReaderHandler)
 */
public interface IStreamingReaderHandler<TEVENT> extends IDisposable {
    /**
     * This method is invoked each time an event is received from the stream.  It is the method 
     * that should handle received events from the stream.
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

	/**
	 * Gets the type of the event which the handler is intended to receive collection of.  
	 * Must be a non-abstract,  non-generic type.  Only events of the exact type
	 * will be received. I.e. events that are sub-types of the returned type 
 	 * will not be received.
	 * 
	 * @return The Class of the event to be handled.
	 */
   Class<TEVENT> getEventType();
}

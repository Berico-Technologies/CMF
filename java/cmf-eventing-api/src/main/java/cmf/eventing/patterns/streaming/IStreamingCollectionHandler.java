package cmf.eventing.patterns.streaming;

import java.util.Collection;

/**
 * Defines an interface to be implemented by types that wish to process each received event stream  
 * as a complete collection, all at once, after all events in the stream are received.
 * <p>
 *     This provides a way to gather all events belonging to a particular stream and deliver them
 *     as a cohesive collection.
 * </p>
 * <p>
 *     A drawback, however, is that because it's holding the collection until all events in the stream
 *     have been received, it introduces a higher degree of latency into the process.
 *     This can be especially apparent the larger the sequence becomes.
 * </p>
 * <p>
 *     The generic {@link java.util.Collection} is of type {@link StreamingEventItem} comes sorted based
 *     on the position flag set in each event header. The position along with each event's own headers
 *     can be obtained through the {@link StreamingEventItem}
 * </p>
 * 
 * WARNING: The streaming event API and its accompanying implementation is deemed 
 * to be a proof of concept at this point and subject to change.  It should not be used 
 * in a production environment. 
 */
public interface IStreamingCollectionHandler<TEVENT> {
    /**
     * This method is invoked after all event in a particular stream of the handled type is received.  
     * It is the method that should handle the received stream of events.
     * @param events The collections of events that all belong to the same stream.
     */
    void handleCollection(Collection<StreamingEventItem<TEVENT>> events);

    /**
     * Enables subscribers with the ability to know how many events have been processed to date.
     * @param percent Percent of events processed so far.
     */
    void onPercentCollectionReceived(double percent);

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

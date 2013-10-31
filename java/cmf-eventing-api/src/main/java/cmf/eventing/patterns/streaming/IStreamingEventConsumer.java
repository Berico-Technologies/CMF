package cmf.eventing.patterns.streaming;

import cmf.eventing.IEventConsumer;

/**
 * Adds behavior to the {@link cmf.eventing.IEventConsumer} that enables subscribers of
 * events to receive a stream of events and store them in a collection or
 * to stream reader.
 * 
 * <p>
 *     WARNING: The streaming event API and its accompanying implementation is deemed 
 *     to be a proof of concept at this point and subject to change.  It should not be used 
 *     in a production environment.
 * </p>
 * @see IStreamingEventBus
 * @see IStreamingEventProducer
 */
public interface IStreamingEventConsumer extends IEventConsumer {

    /**
     * Subscribe to a stream of events that will be collected into a {@link java.util.Collection} 
     * and handed to an IStreamingCollectionHandler implementation for handling once all are 
     * received.
     * <p>See {@link IStreamingCollectionHandler#onPercentCollectionReceived(double)} as a way to
     * get a progress check on how many events have been received in to the collection.
     * </p>
     * <p>
     *     This offers the subscriber a simpler API that {@link #subscribeToReader(IStreamingReaderHandler)}
     *     allowing them to get the entire collection of events once the last one has been received 
     *     from the producer.
     * </p>
     * @param handler The streaming event handler that will handle the collection of events.
     * @param <TEVENT> The type of event to be received.
     * @throws Exception
     * @see IStreamingEventProducer#publishChunkedSequence(java.util.Collection)
     */
    <TEVENT> void subscribeToCollection(IStreamingCollectionHandler<TEVENT> handler) throws Exception;

    /**
     * Subscribe to a stream of events that will get handled by a {@link IStreamingReaderHandler}.
     * <p>
     *     This offers the subscriber a lower latency API to immediately pull and handle events from 
     *     a stream as they arrive.
     * </p>
     * @param handler The streaming event handler that will handle the received events.
     * @param <TEVENT> The type of event to be received.
     * @throws Exception
     * @see IEventStream#publish(Object)
     * @see IStreamingEventProducer#createStream(String)
     */
    <TEVENT> void subscribeToReader(IStreamingReaderHandler<TEVENT> handler) throws Exception;


}

package cmf.eventing.patterns.streaming;

import cmf.eventing.IEventProducer;

import java.util.Collection;

/**
 * Adds behavior to the {@link cmf.eventing.IEventProducer} that enables publishing
 * events as a stream.
 * <p>
 *     This can be useful in scenarios where data becomes available incrementally
 *     and/or the total amount of data to be send is not known ahead of time.
 *     By publishing a data to a stream, the consumer can start to receive and
 *     handle the results as they arrive.
 * </p>
 * <p>
 *     Generally, this pattern is useful for larger responses that could
 *     include some latency in getting everything packaged up in a single
 *     response.
 * </p>
 * 
 * WARNING: The streaming event API and its accompanying implementation is deemed 
 * to be a proof of concept at this point and subject to change.  It should not be used 
 * in a production environment. 
 * @see IStreamingEventBus
 * @see IStreamingEventConsumer
 */
public interface IStreamingEventProducer extends IEventProducer {

    /**
     * Creates an event stream that can be used to publish to a stream of events.
     * {@link IEventStream}s are useful when there is a need to publish an unknown number
     * of events into a stream or there may be significant latency in preparing 
     * individual events.
     * @param topic The topic to which events in the stream should be published.
     * @return The {@link IEventStream} that was created.
     * @see IStreamingEventConsumer#subscribeToReader(IStreamingReaderHandler)
     */
	//TODO: should this not be type based vs topic based?
    IEventStream createStream(String topic);


    /**
     * Publishes a collection of events as a stream of known length.  Useful when the 
     * complete set of events is available immediately but the total size of the data 
     * could make publishing it all as a single event prohibitive.
     * @param dataSet The collection of events to publish.
     * @throws Exception
     * @see IStreamingEventConsumer#subscribeToCollection(IStreamingCollectionHandler)
     */
    public <TEVENT> void publishChunkedSequence(Collection<TEVENT> dataSet) throws Exception;

}

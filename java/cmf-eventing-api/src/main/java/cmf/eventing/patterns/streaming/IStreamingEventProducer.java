package cmf.eventing.patterns.streaming;

import cmf.eventing.IEventProducer;

import java.util.Collection;

/**
 * Adds behavior to the {@link cmf.eventing.IEventProducer} that enables publishing
 * events as a stream.
 * <p>
 *     This can be useful in scenarios where data is being processed as a response
 *     to a request event but the end of that response is not yet known.
 *     By publishing a response to a stream, the consumer can start to
 *     handle the results as opposed to potentially waiting a long time
 *     before anything comes back.
 * </p>
 * <p>
 *     Generally, this pattern is useful for larger responses that could
 *     include some latency in getting everything packaged up in a single
 *     response.
 * </p>
 */
public interface IStreamingEventProducer extends IEventProducer {

    /**
     * Creates an event stream that can be used to publish to a stream of events.
     * {@IEventStream}s are useful when there is a need to publish an unknown number
     * of events into a stream or there may be significant latency in preparing 
     * individual events.
     * @param topic The topic upon which events in the stream should be published.
     * @return
     */
    IEventStream createStream(String topic);


    /**
     * Publishes a collection of events as a stream of known length.  Useful when the 
     * complete set of events is available immediately but the total size of the data 
     * could make publishing it all as a single event prohibitive.
     * @param dataSet The collection of events to publish.
     * @throws Exception
     */
    public <TEVENT> void publishChunkedSequence(Collection<TEVENT> dataSet) throws Exception;

}

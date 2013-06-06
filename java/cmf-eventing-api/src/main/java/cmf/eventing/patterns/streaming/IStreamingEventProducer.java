package cmf.eventing.patterns.streaming;

import cmf.eventing.IEventProducer;

import java.util.Iterator;

/**
 * Adds behavior to the {@link cmf.eventing.IEventProducer} with the ability
 * to publish events to a stream.
 * <p>
 *     This can be useful in scenarios where data is being processed as a response
 *     to a request event but the end of that response is not yet known.
 *     By publishing a response to a stream, the consumer can start to
 *     handle the results as opposed to potentially waiting a long time
 *     before anything comes back.
 * </p>
 * <p>
 *     Generally, this pattern is useful for larger responses that could
 *     include some latency in getting everything packaged up in 1 single
 *     response.
 * </p>
 * User: jholmberg
 * Date: 6/5/13
 */
public interface IStreamingEventProducer extends IEventProducer {
    /**
     * Iterates on the eventStream and calls the object mapper to convert the object to the desired format before
     * publishing the event to the bus.
     * <p>
     *     In addition 3 new headers will be added to each event that is published:<br />
     *     <ol>
     *         <li>sequenceId : A UUID that ties this event to a particular sequence. This is the unique identifier that
     *         indicates the message is part of a larger data set</li>
     *         <li>position : An integer that indicates what position in the sequence this is.</li>
     *         <li>isLast : A boolean indicating if this event is the last message in the sequence</li>
     *     </ol>
     * </p>
     * @param eventStream
     * @param objectMapper
     * @param <TEVENT>
     * @throws Exception
     */
    public <TEVENT> void publishStream(Iterator<Object> eventStream, IStreamingMapperCallback<TEVENT> objectMapper) throws Exception;

    /**
     * Publishes messages on the {@link IStreamingEventBus} after the numberOfEvents limit has been met.
     * @param numberOfEvents
     */
    public void setBatchLimit(int numberOfEvents);
}

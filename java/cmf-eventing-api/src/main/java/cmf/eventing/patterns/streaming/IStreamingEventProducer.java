package cmf.eventing.patterns.streaming;

import cmf.eventing.IEventProducer;

import java.util.Collection;
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
     * Generate an event stream that can be used to publish to the {@link IStreamingEventBus}
     * @param topic
     * @return
     */
    IEventStream createStream(String topic);


    /**
     * Iterates on the dataSet and publishes the elements to the bus.
     * <p>
     *     In addition 2 new headers will be added to each event that is published:<br />
     *     <ol>
     *         <li>cmf.bus.message.pattern#streaming.sequenceId : A UUID that ties this event to a particular sequence. This is the unique identifier that
     *         indicates the message is part of a larger data set</li>
     *         <li>cmf.bus.message.pattern#streaming.position : An integer that indicates what position in the sequence this is.</li>
     *     </ol>
     * </p>
     * @param dataSet
     * @throws Exception
     */
    public <TEVENT> void publishChunkedSequence(Collection<TEVENT> dataSet) throws Exception;

}

package cmf.eventing;

import java.util.Iterator;

/**
 * Adds behavior to the {@link cmf.eventing.IEventBus} enabling it to publish events to a stream.
 * This can be particularly useful when you have a large amount of data in a response
 * and would prefer it to be streamed to the recipient in smaller "byte-sized" chunks.
 *
 * User: jholmberg
 * Date: 6/1/13
 */
public interface IStreamingEventBus extends IEventBus {
    /**
     * Iterates on the eventStream and calls the object mapper to convert the object to the desired format before
     * publishing the event to the bus.
     * <p>
     *     In addition 3 new headers will be added to each event that is published:<br />
     *     <ol>
     *         <li>sequenceId : A UUID that ties this event to a particular sequence. This is the unique identifier that
     *         indicates the message is part of a larger data set</li>
     *         <li>position : An integer that indicates what position in the sequence this is.</li>
     *         <li>end : A boolean indicating if this event is the last message in the sequence</li>
     *     </ol>
     * </p>
     * @param eventStream
     * @param objectMapper
     * @param <TEVENT>
     * @throws Exception
     */
    public <TEVENT> void publishToStream(Iterator<Object> eventStream, IStreamingMapperCallback<TEVENT> objectMapper) throws Exception;

    /**
     * Publishes messages on the {@link IStreamingEventBus} after the numberOfEvents limit has been met.
     * @param numberOfEvents
     */
    public void setBatchLimit(int numberOfEvents);
}

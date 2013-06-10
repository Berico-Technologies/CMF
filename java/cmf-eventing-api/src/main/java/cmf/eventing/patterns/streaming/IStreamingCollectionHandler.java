package cmf.eventing.patterns.streaming;

import cmf.eventing.IEventHandler;

import java.util.Collection;
import java.util.Map;

/**
 * Adds behavior to the {@link cmf.eventing.IEventHandler} allowing it to process
 * a stream of events sent via the {@link IStreamingEventBus}
 * User: jholmberg
 * Date: 6/4/13
 */
public interface IStreamingCollectionHandler<TEVENT> extends IEventHandler<TEVENT> {
    /**
     * Aggregates all events of type TEVENT and stores them into a {@link java.util.Collection}
     * when the last event was received with the message header "isLast" set to true.
     * <p>
     *     This provides a way to gather all events belonging to a particular sequence and deliver them
     *     and a cohesive collection.
     * </p>
     * <p>
     *     A drawback, however, is that because it's holding the collection until all events in the stream
     *     have been received, it introduces a higher degree of latency into the process.
     *     This can be especially apparent the larger the sequence becomes.
     * </p>
     * @param events The collections of events that all belong to the same sequence
     * @param headers
     * @return
     */
    Object handleCollection(Collection<IStreamingEventItem<TEVENT>> events, Map<String, String> headers);

    /**
     * Enables subscribers with the ability to know how many events have
     * been processed to date.
     * @return
     */
    IStreamingProgressNotifier getProgressNotifier();

    void setStreamingProgressUpdater(IStreamingProgressNotifier progressNotifier);

}

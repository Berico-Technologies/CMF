package cmf.eventing.patterns.streaming;

import cmf.bus.IDisposable;


public interface IEventStream extends IDisposable {
    /**
     * Publishes messages on the {@link IStreamingEventBus} after the numberOfEvents limit has been met.
     * @param numberOfEvents
     */
    void setBatchLimit(int numberOfEvents);

    /**
     * Called by the {@link IStreamingEventBus} to publish each element in the Iterator in a searializable format.
      * @param event
     */
    void publish(Object event) throws Exception;
}

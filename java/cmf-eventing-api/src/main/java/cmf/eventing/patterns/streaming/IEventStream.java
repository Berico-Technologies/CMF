package cmf.eventing.patterns.streaming;

import cmf.bus.IDisposable;

/**
 * An interface representing an single event stream and defining the methods needed 
 * to publish events individually into that stream.  
 * 
 * WARNING: The streaming event API and its accompanying implementation is deemed 
 * to be a proof of concept at this point and subject to change.  It should not be used 
 * in a production environment. 
 */
public interface IEventStream extends IDisposable {
    /**
     * Determines the number of events that must be {@link #publish}ed to the stream buffer 
     * before the buffer is actually flushed to the underlying transport.
     * @param numberOfEvents
     */
    void setBatchLimit(int numberOfEvents);

    /**
     * Publishes an event into the event stream.  Depending on the values assigned via
     * {@link #setBatchLimit(int)}, the event may be buffered prior to being send on the 
     * underlying transport.
     * @param event The event to publish.
     */
    void publish(Object event) throws Exception;

    /**
     * Gets the message topic upon which the events will be published.
     */
    String getTopic();

    /**
     * Gets the unique sequence identifier that all messages published in this stream 
     * will be tagged with.
     */
    String getSequenceId();
}

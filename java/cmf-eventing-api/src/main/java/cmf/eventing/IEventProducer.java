package cmf.eventing;

import java.util.Map;

import cmf.bus.IDisposable;

/**
 * An interface defining the methods by which an client may publish events.
 */
public interface IEventProducer extends IDisposable {

	/**
	 * Publishes an event.
	 * 
	 * @param event The event to publish.
	 * 
	 * @throws Exception
	 */
    void publish(Object event) throws Exception;


    /**
     * Publishes an event and specifies a set of custom headers to send with it.
     * Normally the headers to be sent with an event are computed by the 
     * IEventProducer implementation.  If custom headers are provided, how they 
     * are combined with any computed headers is implementation dependent.
     *
     * @param event The event to publish.
     * @param headers The custom headers to publish with it.
     *
     * @throws Exception
     */
    void publish(Object event, Map<String, String> headers) throws Exception;
}

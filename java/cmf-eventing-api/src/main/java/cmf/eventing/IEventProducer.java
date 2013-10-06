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
     * Publishes an event with the specified headers.
     *
     * @param event The event to publish.
     * @param headers The custom headers to publish.
     *
     * @throws Exception
     */
    void publish(Object event, Map<String, String> headers) throws Exception;
}

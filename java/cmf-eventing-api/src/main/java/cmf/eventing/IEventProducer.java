package cmf.eventing;

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
}

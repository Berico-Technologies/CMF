package cmf.eventing;

/**
 * An interface defining the methods by which an client may public events.
 */
public interface IEventProducer {

	/**
	 * Publishes an event.
	 * 
	 * @param event The event to publish.
	 * 
	 * @throws Exception
	 */
    void publish(Object event) throws Exception;
}

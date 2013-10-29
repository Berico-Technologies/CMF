package cmf.eventing;

import java.util.Map;

import cmf.bus.Envelope;

/**
 * Defines an interface to be implemented by types that which to process received events. 
 */
public interface IEventHandler<TEVENT> {

	/**
	 * Returns the type of the event which the handler is intended to receive.  
	 * Must be a non-abstract,  non-generic type.  Only events of the exact type
	 * will be received. I.e. events that are sub-types of the returned type 
 	 * will not be received.
	 * 
	 * @return The Class of the event to be handled.
	 */
    Class<TEVENT> getEventType();

    /**
     * The method that processes the received event.
     * 
     * @param event The event to be processed.
     * @param headers The {@link cmf.bus.Envelope} headers of the envelope in which
     * the event arrived. 
     * 
     * @return An object indicating the outcome of handling the event.
     */
    Object handle(TEVENT event, Map<String, String> headers);

    /**
     * This method will be invoked when an exception occurs attempting to handle an 
     * envelope that meets the registration and filter criteria.
     * 
     * @param env The envelope in which the event was received.
     * @param ex The exception that occurred.
     * 
     * @return An object indicating the outcome of handling the envelope.
     */
    Object handleFailed(Envelope envelope, Exception e);
    //TODO: This method is never called by the underlying implementation.
}

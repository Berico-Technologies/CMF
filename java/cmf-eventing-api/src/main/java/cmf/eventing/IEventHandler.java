package cmf.eventing;

import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeReceiver;

/**
 * Defines an interface to be implemented by types that wish to process received events. 
 */
public interface IEventHandler<TEVENT> {

	/**
	 * Gets the type of the event which the handler is intended to receive.  
	 * Must be a non-abstract,  non-generic type.  Only events of the exact type
	 * will be received. I.e. events that are sub-types of the returned type 
 	 * will not be received.
	 * 
	 * @return The Class of the event to be handled.
	 */
    Class<TEVENT> getEventType();

    /**
     * This method is invoked when an event of handled type is received.  It is   
     * the method that should handle the received event.
     * 
     * @param event The event to be processed.
     * @param headers The {@link cmf.bus.Envelope} headers of the envelope in which
     * the event arrived. 
     * 
     * @return An object indicating the outcome of handling the event.  How the return
     * value is interpreted is dependent upon the {@link IEventConsumer} implementation.
     */
    Object handle(TEVENT event, Map<String, String> headers);

    /**
     * This method is invoked when an exception occurs attempting to handle an 
     * envelope that meets the registration and filter criteria.  The relationship 
     * between this method and the {@link cmf.bus.IEnvelopeHandler#handleFailed} method 
     * is implementation dependent.
     * 
     * @param env The envelope in which the event was received.
     * @param ex The exception that occurred.
     * 
     * @return An object indicating the outcome of handling the envelope.
     */
    Object handleFailed(Envelope envelope, Exception e);
}

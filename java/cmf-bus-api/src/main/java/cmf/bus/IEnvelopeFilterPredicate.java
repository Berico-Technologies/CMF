package cmf.bus;

/**
 * Defines an interface for predicates used to filter which received messages are 
 * handled by a particular {@link IRegistration}.
 */
public interface IEnvelopeFilterPredicate {

    /**
     * Indicates if the particular envelope should be processed by the envelope
     * processing change and ultimately handed to the {@link IRegistration}s 
     * handler method for handling.
     * 
     * @param envelope The raw envelope that has been received.
     * @return True if the envelope should be processed by the handler,
     * otherwise false.
     * @see Envelope
     */
    boolean filter(Envelope envelope);
}

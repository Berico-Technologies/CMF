package cmf.bus;

/**
 * Defines an interface for predicates used to filter which received messages are 
 * handled by a particular {@link IRegistration}.
 */
public interface IEnvelopeFilterPredicate {

    /**
     * Offers an opportunity to filter out the envelope before it gets processed.
     * 
     * @param envelope The envelope that has been receieved.
     * @return True if the envelope should be processed by the handler,
     * otherwise false.
     */
    boolean filter(Envelope envelope);
}

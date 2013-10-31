package cmf.bus;

import java.util.Map;

/**
 * Defines the interface that must be implemented by types that wish to 
 * {@link IEnvelopeReceiver#register} to receive envelopes.
 * @see IEnvelopeReceiver#unregister(IRegistration)
 */public interface IRegistration {

	/**
	 * Specifies a filter predicate, if any, that should be used to select which received 
	 * envelopes should be forwarded to the handle method for processing.  Filtering occurs before
	 * the envelope processing chain is invoked.  Its judicious use can therefore eliminate
	 * unnecessary processing of unwanted messages and improve performance.
	 * @return The {@link IEnvelopeFilterPredicate} instance to use to as a filter or null.
	 * If null is returned, all received envelopes that match the registrations registration info
	 * (See {@link IRegistration#getRegistrationInfo()}) will be processed.
	 */
    IEnvelopeFilterPredicate getFilterPredicate();

    /**
     * Gets the key/value set of properties that describe the envelopes to be received.  The meaning of
     * these values are dependent upon the {@link IEnvelopeReceiver} implementation.
     */
    Map<String, String> getRegistrationInfo();

    /**
     * This method will be invoked when an envelope meeting the registration and filter 
     * criteria is received.
     * 
     * @param env The envelope that has been received.
     * 
     * @return An object indicating the outcome of handling the envelope.  How the return
     * value is interpreted is dependent upon the {@link IEnvelopeReceiver} implementation.
     * 
     * @throws Exception
     */
    Object handle(Envelope env) throws Exception;

    /**
     * This method will be invoked when an exception occurs attempting to handle an 
     * envelope that meets the registration and filter criteria.
     * 
     * @param env The envelope to be handled.
     * @param ex The exception that occurred.
     * 
     * @return An object indicating the outcome of handling the envelope.  How the return
     * value is interpreted is dependent upon the {@link IEnvelopeReceiver} implementation.
     * 
     * @throws Exception
     */
    Object handleFailed(Envelope env, Exception ex) throws Exception;

    // IEnvelopeHandler getHandler();
    //
    // void setFilterPredicate(IEnvelopeFilterPredicate envelopeFilterPredicate);
    //
    // void setHandler(IEnvelopeHandler envelopeHandler);
    //
    // void setRegistrationInfo(Map<String, String> registrationInfo);
}

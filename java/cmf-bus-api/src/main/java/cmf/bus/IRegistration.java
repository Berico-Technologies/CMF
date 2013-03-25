package cmf.bus;

import java.util.Map;

/**
 * Defines the interface type that is needed in order to {@link IEnvelopeReceiver#Register}
 * to receive envelopes.
 */public interface IRegistration {

	/**
	 * Specified a filter predicate, if any, that should be used to select which received 
	 * envelopes should be forwarded to the handler for processing.
	 * @return
	 */
    IEnvelopeFilterPredicate getFilterPredicate();

    /**
     * A key/value set of properties that describe the evenlopes to be recieved.
     * @return
     */
    Map<String, String> getRegistrationInfo();

    /**
     * This method will be invoked when an envelope meeting the registration and filter 
     * criteria is received.
     * 
     * @param env The envelope that has been received.
     * 
     * @return An object indicating the outcome of handling the envelope.
     * 
     * @throws Exception
     */
    Object handle(Envelope env) throws Exception;

    /**
     * This method will be invoked when an exception occurs attempting to handle an 
     * envelope that meets the registration and filter criteria.
     * 
     * @param env The envelope that was received.
     * @param ex The execption that occured.
     * 
     * @return An object indicating the outcome of handling the envelope.
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

package cmf.eventing.patterns.rpc;

import java.util.Collection;

import org.joda.time.Duration;

/**
 * An interface to define the methods by which an client may send RPC messages and 
 * receive responses to the same.
 */
public interface IRpcReceiver {

	/**
	 * Sends an RPC message and returns all responses of an expected type that are 
	 * received within a given time period.
	 * 
	 * @param request The RPC message to be sent.
	 * @param timeout The duration of time to wait for messages before returning.
	 * 
	 * @return Returns the collection of response messages received prior to the timeout being reached.
	 */
    <TResponse> Collection<TResponse> gatherResponsesTo(Object request, Duration timeout);

    
	/**
	 * Sends an RPC message and returns all responses of any types that are received 
	 * on a list of topics within a given time period.
	 * 
	 * @param request The RPC message to be sent.
	 * @param timeout The duration of time to wait for messages before returning.
     * @param expectedTopics One or more topics to listen to for responses.
	 * 
	 * @return Returns the collection of response messages received prior to the timeout being reached.
	 */
    @SuppressWarnings("rawtypes")
    Collection gatherResponsesTo(Object request, Duration timeout, String... expectedTopics);

    /**
     * Sends an RPC message and returns the first response received of an expected type.
     * 
	 * @param request The RPC message to be sent.
	 * @param timeout The duration of time to wait for messages before returning.
     * @param expectedType The type of the expected response.
     * 
     * @return The first response message received.
     */
    <TResponse> TResponse getResponseTo(Object request, Duration timeout, Class<TResponse> expectedType);

    /**
     * Sends an RPC message and returns the first response of any type that is received
     * on the specified topic.
     * 
	 * @param request The RPC message to be sent.
	 * @param timeout The duration of time to wait for messages before returning.
     * @param expectedTopic The topic on which to listen for the response message.
     * 
     * @return The first response message received.
     */
    Object getResponseTo(Object request, Duration timeout, String expectedTopic);
}

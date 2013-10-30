package cmf.eventing.patterns.rpc;

import java.util.Map;

/**
 * An interface defining the methods by which a client responds to RPC calls.
 */
public interface IRpcSender {

	/**
	 * Sends a response to a specific RPC request.
	 * 
	 * @param headers The headers from the received event that contained the RPC 
	 * request. (Used to route the response back to the appropriate caller.)
	 * @param response The response to be sent.
	 */
    void respondTo(Map<String, String> headers, Object response);
}

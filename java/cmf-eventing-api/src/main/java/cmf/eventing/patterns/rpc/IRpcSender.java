package cmf.eventing.patterns.rpc;

import java.util.Map;

/**
 * An interface defining the methods by which a client responds to RPC calls.
 */
//TODO: Consider renaming to IRpcResponder
public interface IRpcSender {

	/**
	 * Called to respond to an RPC request.
	 * 
	 * @param headers The headers from the received envelope that contained the RPC 
	 * request. (Used to route the request back to the appropriate sender.)
	 * @param response The response message to be sent.
	 */
    void respondTo(Map<String, String> headers, Object response);
}

package cmf.eventing.patterns.rpc;

import java.util.Map;

import cmf.eventing.IEventHandler;

/**
 * <p>An interface defining the methods by which a client responds to RPC calls.</p>
 * <p>Typically an instance of this interface used by implementations of 
 * {@link IEventHandler} that handle RPC request in order that they
 * may send responses to those requests.</p> 
 * @see IRpcEventBus
 * @see IRpcReceiver
 */
public interface IRpcSender {

	/**
	 * <p>Sends a response to a specific RPC request.</p>  
	 * <p>This method should be called from 
	 * {@link IEventHandler#handle(Object, Map)} in the course of
	 * handling an RPC type event.</p>
	 * 
	 * @param headers The headers from the received event that contained the RPC 
	 * request. (Used to route the response back to the appropriate caller.)
	 * @param response The response to be sent.
	 */
    void respondTo(Map<String, String> headers, Object response);
}

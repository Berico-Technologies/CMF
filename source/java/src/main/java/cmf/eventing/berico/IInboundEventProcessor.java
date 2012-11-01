package cmf.eventing.berico;

/**
 * Defines the interface of a component that can process incoming events and optionally intercept (deny) incoming events
 * before they reach the client.
 * 
 * @author jruiz
 * 
 */
public interface IInboundEventProcessor {

    /**
     * Implement this method in order to process incoming events before they're given to the client.
     * 
     * @param context
     *            Contains the event, envelope, and properties.
     * @return true if processing may continue, otherwise false.
     * @throws Exception
     *             processing will be cancelled if an exception is thrown
     */
    boolean processInbound(ProcessingContext context) throws Exception;
}

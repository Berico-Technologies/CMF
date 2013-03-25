package cmf.eventing;

/**
 * Defines an interface for predicates used to filter which received events are 
 * handled by a particular {@link IEventHandler}.
 */
public interface IEventFilterPredicate {

    /**
     * Offers an opportunity to filter out the event before it gets processed.
     * 
     * @param event The event that has been received.
     *  
     * @return True if the event should be processed by the handler,
     * otherwise false.
     */
    boolean filter(Object event);
}

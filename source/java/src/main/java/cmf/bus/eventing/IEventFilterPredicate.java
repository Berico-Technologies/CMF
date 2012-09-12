package cmf.bus.eventing;

public interface IEventFilterPredicate {

    boolean filter(Object event);
}

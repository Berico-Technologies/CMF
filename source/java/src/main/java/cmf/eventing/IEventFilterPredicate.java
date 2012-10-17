package cmf.eventing;

public interface IEventFilterPredicate {

    boolean filter(Object event);
}

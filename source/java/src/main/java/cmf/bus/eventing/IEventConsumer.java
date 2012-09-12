package cmf.bus.eventing;

public interface IEventConsumer {

    <TEVENT> void subscribe(IEventHandler<TEVENT> handler);

    <TEVENT> void subscribe(IEventHandler<TEVENT> handler, IEventFilterPredicate eventFilterPredicate);
}

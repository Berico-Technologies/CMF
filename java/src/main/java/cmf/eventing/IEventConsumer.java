package cmf.eventing;

public interface IEventConsumer {

    <TEVENT> void subscribe(IEventHandler<TEVENT> handler) throws Exception;

    <TEVENT> void subscribe(IEventHandler<TEVENT> handler, IEventFilterPredicate eventFilterPredicate) throws Exception;
}

package cmf.bus.core.event;

public interface IEventBus {

    <EVENT> void register(IEventHandler<EVENT> eventHandler, Class<EVENT> type);

    void send(Object event);

}

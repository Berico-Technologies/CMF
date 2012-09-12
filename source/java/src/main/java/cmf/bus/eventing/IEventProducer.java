package cmf.bus.eventing;

public interface IEventProducer {

    void publish(Object event);
}

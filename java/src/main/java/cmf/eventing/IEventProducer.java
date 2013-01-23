package cmf.eventing;

public interface IEventProducer {

    void publish(Object event) throws Exception;
}

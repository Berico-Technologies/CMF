package cmf.eventing;

import cmf.bus.IDisposable;

/**
 * A convenience interface that combines the {@link IEventProducer} and {@link IEventConsumer} 
 * interfaces together because they are most typically implemented and referenced together.
 */
public interface IEventBus extends IEventProducer, IEventConsumer, IDisposable {

}

package cmf.eventing.patterns.streaming;

import cmf.eventing.IEventBus;

/**
 * A convenience interface that extends the {@link IEventBus} to include both the 
 * {@link IStreamingEventProducer} and {@link IStreamingEventConsumer} extensions to
 * {@link cmf.eventing.IEventProducer} and {@link cmf.eventing.IEventConsumer} interfaces respectively.
 * */
public interface IStreamingEventBus extends IEventBus, IStreamingEventConsumer, IStreamingEventProducer {

}

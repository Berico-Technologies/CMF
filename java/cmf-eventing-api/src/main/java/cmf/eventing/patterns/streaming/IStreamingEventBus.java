package cmf.eventing.patterns.streaming;

import cmf.eventing.IEventBus;

/**
 * A convenience interface that extends the {@link IEventBus} to include both the 
 * {@link IStreamingEventProducer} and {@link IStreamingEventConsumer} extensions to
 * {@link cmf.eventing.IEventProducer} and {@link cmf.eventing.IEventConsumer} interfaces respectively.
 * 
 * WARNING: The streaming event API and its accompanying implementation is deemed 
 * to be a proof of concept at this point and subject to change.  It should not be used 
 * in a production environment. 
 * */
public interface IStreamingEventBus extends IEventBus, IStreamingEventConsumer, IStreamingEventProducer {

}

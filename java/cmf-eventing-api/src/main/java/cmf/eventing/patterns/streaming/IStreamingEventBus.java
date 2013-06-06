package cmf.eventing.patterns.streaming;

import cmf.eventing.IEventBus;

/**
 * Adds behavior to the {@link cmf.eventing.IEventBus} enabling it to publish events as a stream of messages
 * over the network before the entire result has been processed.
 * This can be particularly useful when you have a large amount of data in a response
 * and would prefer it to be streamed to the recipient in smaller chunks to lower initial latency in response times.
 *
 * User: jholmberg
 * Date: 6/1/13
 */
public interface IStreamingEventBus extends IEventBus, IStreamingEventConsumer, IStreamingEventProducer {

}

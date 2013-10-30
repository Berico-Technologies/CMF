using System.Collections.Generic;

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// Adds behavior to the <see cref="cmf.eventing.IEventProducer"/> that enables 
    /// publishing events to a stream.
    /// <para>
    /// This can be useful in scenarios where data becomes available incrementally
    /// and/or the total amount of data to be send is not known ahead of time.
    /// By publishing a data to a stream, the consumer can start to receive and
    /// handle the results as they arrive.
    /// </para>
    /// <para>
    /// Generally, this pattern is useful for larger responses that could
    /// include some latency in getting everything packaged up in a single
    /// response.
    /// </para>
    /// <para>WARNING: The streaming event API and its accompanying implementation is deemed 
    /// to be a proof of concept at this point and subject to change.  It should not be used 
    /// in a production environment.</para>
    /// </summary>
    public interface IStreamingEventProducer : IEventProducer
    {
        /// <summary>
        /// Creates an event stream that can be used to publish to a stream of events. 
        /// <see cref="IEventStream" />s are useful when there is a 
        /// need to publish an unknown number of events into a stream or there may be 
        /// significant latency in preparing individual events.
        /// </summary>
        /// <param name="topic">The topic to which events in the stream should be published.</param>
        /// <returns>The <see cref="IEventStream" /> that was created.</returns>
        IEventStream CreateStream(string topic);

        /// <summary>
        /// Publishes a collection of events as a stream of known length. Useful when the 
        /// complete set of events is available immediately but the total size of the dat
        /// a could make publishing it all as a single event prohibitive.
        /// </summary>
        /// <typeparam name="TEvent">The type of the events being published.</typeparam>
        /// <param name="dataSet">The collection of events to publish.</param>
        /// <exception cref="System.Exception">May throw an exception.</exception>
        void PublishChunkedSequence<TEvent>(ICollection<TEvent> dataSet);

    }
}

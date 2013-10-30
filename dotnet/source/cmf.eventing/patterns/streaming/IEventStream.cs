using System;

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// An interface representing an single event stream and defining the methods 
    /// needed to publish events individually into that stream.
    /// <para>WARNING: The streaming event API and its accompanying implementation is deemed 
    /// to be a proof of concept at this point and subject to change.  It should not be used 
    /// in a production environment.</para> 
    /// </summary>
    public interface IEventStream : IDisposable
    {
        /// <summary>
        /// Determines the number of events that must be publish to the stream buffer 
        /// before the buffer is actually flushed to the underlying transport.
        /// </summary>
        int BatchLimit { set; }

        /// <summary>
        /// Publishes an event into the event stream. Depending on the value of BatchLimit,
        /// the event may be buffered prior to being send on the underlying transport.
        /// </summary>
        /// <param name="evt"></param>
        void Publish(object evt);

        /// <summary>
        /// The message topic upon which the events will be published.
        /// </summary>
        string Topic { get; }

        /// <summary>
        /// The unique sequence identifier that all messages published in this stream will be tagged with.
        /// </summary>
        string SequenceId { get; }
    }
}

using System;
using System.Collections.Generic;

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// Defines an interface to be implemented by types that wish to process each received event stream 
    /// as a complete collection, all at once, after all events in the stream are received.
    /// 
    /// <para>WARNING: The streaming event API and its accompanying implementation is deemed 
    /// to be a proof of concept at this point and subject to change.  It should not be used 
    /// in a production environment.</para>
    /// </summary>
    /// <remarks>
    /// This provides a way to gather all events belonging to a particular stream and deliver them as a cohesive collection.
    /// <para>
    /// A drawback, however, is that because it's holding the collection until all events 
    /// in the stream have been received, it introduces a higher degree of latency into 
    /// the process. This can be especially apparent the larger the sequence becomes.
    /// </para>
    /// <para>
    /// The generic IEnumerable is of type StreamingEventItem comes sorted based on the 
    /// position flag set in each event header. The position along with each event's own
    /// headers can be obtained through the StreamingEventItem.
    /// </para>
    /// </remarks>
    public interface IStreamingCollectionHandler<TEvent>
    {
        /// <summary>
        /// This method is invoked after all event in a particular stream of the handled type is received. 
        /// It is the method that should handle the received stream of events. 
        /// </summary>
        /// <param name="events">The collection of events that all belong to the same sequence</param>
        void HandleCollection(IEnumerable<StreamingEventItem<TEvent>> events);

        /// <summary>
        /// Enables subscribers with the ability to know how many events have been processed to date.
        /// </summary>
        /// <param name="percent">Percent of events processed so far.</param>
        void OnPercentCollectionReceived(double percent);

        /// <summary>
        /// The type of the event which the handler is intended to receive collection of.  
        /// Must be a non-abstract,  non-generic type.  Only events of the exact type
        /// will be received. I.e. events that are sub-types of the returned type 
        /// will not be received.
        /// </summary>
        Type EventType { get; }
    }
}

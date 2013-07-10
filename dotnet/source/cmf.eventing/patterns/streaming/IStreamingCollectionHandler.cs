using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.eventing;

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// Adds behavior to the <see cref="cmf.eventing.IEventHandler"/> allowing it to process
    /// a stream of events sent via the <see cref="IStreamingEventBus"/>
    /// User: jholmberg
    /// Date: 6/10/13
    /// </summary>
    public interface IStreamingCollectionHandler<TEvent>
    {
        /// <summary>
        /// Aggregates all events of type TEvent and stores them into a <see cref="System.Collections.Generic.IEnumerable"/>
        /// when the last event was received with the message header "isLast" set to true.
        /// <para>
        /// This provides a way to gather all events belonging to a particular sequence and deliver them 
        /// in one cohesive collection.
        /// </para>
        /// <para>
        /// One drawback, however, is that because it's holding the collection until all events in the stream
        /// have been received, it introduces a higher degree of latency into the process.
        /// This can be especially apparent the larger the sequence becomes.
        /// </para>
        /// </summary>
        /// <param name="events">The collection of events that all belong to the same sequence</param>
        /// <returns></returns>
        void HandleCollection(IEnumerable<StreamingEventItem<TEvent>> events);

        /// <summary>
        /// Enables subscribers with the ability to know how many events have been processed to date.
        /// </summary>
        /// <param name="percent">Percent of events processed so far.</param>
        void OnPercentCollectionReceived(double percent);

        Type EventType { get; }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// Contains the elements needed to receive an event from an event stream.
    /// This allows each discrete event pulled from a subscribed event stream to 
    /// contain the necessary state that a subscruber would need including the 
    /// sequenceId, position, and whether this event is the last in the published 
    /// sequence.
    /// User: jholmberg
    /// Date: 6/7/13
    /// </summary>
    /// <typeparam name="TEvent"></typeparam>
    public interface IStreamingEventItem<TEvent>
    {
        /// <summary>
        /// Get the event received from the published stream
        /// </summary>
        TEvent Event { get; }

        /// <summary>
        /// Helper method that extracts the unique sequenceId from the event headers
        /// </summary>
        Guid SequenceId { get; }

        /// <summary>
        /// Helper method that returns the position of this event in the event sequence
        /// </summary>
        int Position { get; }

        /// <summary>
        /// Helper method that returns whether this event is the last in the sequence
        /// </summary>
        bool IsLast { get; }

        /// <summary>
        /// Returns all headers associated with the event
        /// </summary>
        IDictionary<string, string> EventHeaders { get; }
    }
}

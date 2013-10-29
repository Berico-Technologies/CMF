using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// WARNING: The streaming event API and its accompanying implementation is deemed 
    /// to be a proof of concept at this point and subject to change.  It should not be used 
    /// in a production environment. 
    /// </summary>
    public interface IEventStream : IDisposable
    {
        /// <summary>
        /// Publishes messages on the <see cref="IStreamingEventBus"/> after the batch limit has been met.
        /// </summary>
        int BatchLimit { set; }

        /// <summary>
        /// Called by the <see cref="IStreamingEventBus"/> to publish each element in a serializable format.
        /// </summary>
        /// <param name="evt"></param>
        void Publish(object evt);

        /// <summary>
        /// Indicates what message topic to which all events on this stream will be published.
        /// </summary>
        string Topic { get; }

        /// <summary>
        /// Indicates the unique sequence identifier that all messages will be linked to in this stream.
        /// </summary>
        string SequenceId { get; }
    }
}

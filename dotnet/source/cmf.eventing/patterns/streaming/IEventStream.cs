using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing.patterns.streaming
{
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
    }
}

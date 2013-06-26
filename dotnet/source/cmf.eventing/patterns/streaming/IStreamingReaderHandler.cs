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
    /// </summary>
    /// <typeparam name="TEvent"></typeparam>
    public interface IStreamingReaderHandler<TEvent> : IEventHandler, IDisposable
    {
        /// <summary>
        /// Streams all events of type TEvent as they are received from the bus and places them into an 
        /// <see cref="IStreamingEventItem"/> that can be returned immediately to the caller.
        /// <para>
        /// This method will continue to be called until the second to last message in the 
        /// sequence is received. The last message in the sequence will be passed through 
        /// the <see cref="IStreamingReaderHandler#onSequenceFinished(IStreamingEventItem)"/> method.
        /// </para>
        /// <para>
        /// This implementation reduces the latency that the collection based option has but requires 
        /// a little more complex code to handle the results as they are received.
        /// </para>
        /// </summary>
        /// <param name="eventItem"></param>
        /// <returns></returns>
        object OnSequenceEventRead(IStreamingEventItem<TEvent> eventItem);

    }
}

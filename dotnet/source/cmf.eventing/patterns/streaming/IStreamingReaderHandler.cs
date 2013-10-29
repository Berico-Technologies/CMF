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
    /// 
    /// <para>WARNING: The streaming event API and its accompanying implementation is deemed 
    /// to be a proof of concept at this point and subject to change.  It should not be used 
    /// in a production environment.</para>
    /// </summary>
    /// <remarks>
    /// Streams all events of type TEvent as they are received from the bus and places them into an 
    /// <see cref="IStreamingEventItem"/> that can be returned immediately to the caller.
    /// <para>
    /// This implementation reduces the latency of the collection based option.
    /// </para>
    /// </remarks>
    /// <typeparam name="TEvent"></typeparam>
    public interface IStreamingReaderHandler<TEvent> : IDisposable, IObserver<StreamingEventItem<TEvent>>
    {

        Type EventType { get; }
    }
}

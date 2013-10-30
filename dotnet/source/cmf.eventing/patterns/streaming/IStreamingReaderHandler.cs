using System;

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// Defines an interface to be implemented by types that wish to process 
    /// individual events from a stream as they are received with minimal latency. 
    /// 
    /// <para>WARNING: The streaming event API and its accompanying implementation is deemed 
    /// to be a proof of concept at this point and subject to change.  It should not be used 
    /// in a production environment.</para>
    /// </summary>
    /// <remarks>The core functionality of this interface is inherited from 
    /// <see cref="System.IObserver{T}">IObserver</see> whose methods should be implemented to 
    /// receive the streamed events as they arrive.</remarks>
    /// <typeparam name="TEvent">The type of event which the handler is intended to handle.</typeparam>
    public interface IStreamingReaderHandler<TEvent> : IDisposable, IObserver<StreamingEventItem<TEvent>>
    {
        /// <summary>
        /// The type of the event which the handler is intended to receive collection of. 
        /// Must be a non-abstract, non-generic type. Only events of the exact type will be
        /// received. I.e. events that are sub-types of the returned type will not be received.
        /// </summary>
        Type EventType { get; }
    }
}

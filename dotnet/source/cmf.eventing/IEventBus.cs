using System;

namespace cmf.eventing
{
    /// <summary>
    /// A convenience interface that combines the <see cref="IEventProducer" /> 
    /// and <see cref="IEventConsumer" /> interfaces together 
    /// because they are most typically implemented and referenced together.
    /// </summary>
    public interface IEventBus : IEventProducer, IEventConsumer, IDisposable
    {
        
    }
}

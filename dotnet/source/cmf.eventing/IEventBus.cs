using System;

namespace cmf.eventing
{
    /// <summary>
    /// A convenience interface that combines the <see cref="IEventProducer">IEventProducer</see> 
    /// and <see cref="IEventConsumer">IEventConsumer</see> interfaces together 
    /// because they are most typically implemented and referenced together.
    /// </summary>
    public interface IEventBus : IEventProducer, IEventConsumer, IDisposable
    {
        
    }
}

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// A convenience interface that extends the IEventBus to include both the 
    /// <see cref="IStreamingEventProducer">IStreamingEventProducer</see> and 
    /// <see cref="IStreamingEventConsumer">IStreamingEventConsumer</see> 
    /// extensions to <see cref="IEventProducer">IEventProducer</see> and 
    /// <see cref="IEventConsumer">IEventConsumer</see> interfaces respectively.
    /// 
    /// <para>WARNING: The streaming event API and its accompanying implementation is deemed 
    /// to be a proof of concept at this point and subject to change.  It should not be used 
    /// in a production environment.</para>
    /// </summary>
    public interface IStreamingEventBus : IEventBus, IStreamingEventConsumer, IStreamingEventProducer
    {
    }
}

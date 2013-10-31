namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// Adds behavior to the <see cref="cmf.eventing.IEventConsumer"/> that enables subscribers
    /// of events to receive a stream of events and store them in a collection or to stream reader.
    /// 
    /// <para>WARNING: The streaming event API and its accompanying implementation is deemed 
    /// to be a proof of concept at this point and subject to change.  It should not be used 
    /// in a production environment.</para>
    /// </summary>
    /// <seealso cref="IStreamingEventBus"/>
    /// <seealso cref="IStreamingEventProducer"/>
    public interface IStreamingEventConsumer : IEventConsumer
    {
        /// <summary>
        /// Subscribe to a stream of events that will be collected into a <see cref="System.Collections.Generic.IEnumerable{T}"/> 
        /// and handed to an <see cref="IStreamingCollectionHandler"/> implementation for handling once all are received. 
        /// <para>
        /// See <see cref="IStreamingCollectionHandler{TEvent}.OnPercentCollectionReceived"/> as a way to 
        /// get a progress check on how many events have been processed.
        /// </para>
        /// <para>
        /// This offers the subscriber a simpler API than <see cref="SubscribeToReader{TEvent}"/>
        /// allowing them to get the entire collection of events once the last one has been received from the producer.
        /// </para>
        /// </summary>
        /// <typeparam name="TEvent">The type of event to be received.</typeparam>
        /// <param name="handler">The streaming event handler that will handle the collection of events.</param>
        /// <exception cref="System.Exception">May throw an exception.</exception>
        /// <seealso cref="IStreamingEventProducer.PublishChunkedSequence{TEvent}"/>
        void SubscribeToCollection<TEvent>(IStreamingCollectionHandler<TEvent> handler);

        /// <summary>
        /// Subscribe to a stream of events that will get handled by a <see cref="IStreamingReaderHandler{TEvent}"/>.
        /// <para>
        /// This offers the subscriber a lower latency API to immediately pull events from a 
        /// stream as they arrive.
        /// </para>
        /// </summary>
        /// <typeparam name="TEvent">The type of event to be received.</typeparam>
        /// <param name="handler">The streaming event handler that will handle the received events.</param>
        /// <exception cref="System.Exception">May throw an exception.</exception>
        /// <seealso cref="IEventStream.Publish"/>
        /// <seealso cref="IStreamingEventProducer.CreateStream"/>
        void SubscribeToReader<TEvent>(IStreamingReaderHandler<TEvent> handler);
    }
}

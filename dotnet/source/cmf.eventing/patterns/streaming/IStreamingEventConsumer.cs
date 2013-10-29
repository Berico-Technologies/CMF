using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// Adds behavior to the <see cref="cmf.eventing.IEventConsumer"/> that enables subscribers of
    /// events to receive a stream of events and store them in a collection or to stream reader.
    /// 
    /// <para>WARNING: The streaming event API and its accompanying implementation is deemed 
    /// to be a proof of concept at this point and subject to change.  It should not be used 
    /// in a production environment.</para>
    /// </summary>
    public interface IStreamingEventConsumer : IEventConsumer
    {
        /// <summary>
        /// Subscribe to a stream of events that will get handled to a <see cref="System.Collections.Generic.IEnumerable"/>
        /// <para>
        /// See <see cref="IStreamingCollectionHandler.OnPercentCollectionReceived"/> as a way to 
        /// get a progress check on how many events have been processed.
        /// </para>
        /// <para>
        /// This offers the subscriber a simpler API allowing them to get the entire collection 
        /// of events once the last one has been received from the producer.
        /// </para>
        /// </summary>
        /// <typeparam name="TEvent"></typeparam>
        /// <param name="handler"></param>
        void SubscribeToCollection<TEvent>(IStreamingCollectionHandler<TEvent> handler);

        /// <summary>
        /// Subscribe to a stream of events that will get handled by a <see cref="IStreamingReaderHandler"/>.
        /// <para>
        /// This offers the subscriber a lower latency API to immediately pull events from a 
        /// stream as they arrive.
        /// </para>
        /// </summary>
        /// <typeparam name="TEvent"></typeparam>
        /// <param name="handler"></param>
        void SubscribeToReader<TEvent>(IStreamingReaderHandler<TEvent> handler);
    }
}

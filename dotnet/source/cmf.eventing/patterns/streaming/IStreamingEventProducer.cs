using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.eventing;

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// Adds behavior to the <see cref="cmf.eventing.IEventProducer"/> with the ability
    /// to publish events to a stream.
    /// <para>
    /// This can be useful in scenarios where data is being processed as a response 
    /// to a request event but the end of that response is not yet known.
    /// By publishing a respone to a stream, the consumer can start to 
    /// handle the results as opposed to potentially waiting a long time 
    /// before anything comes back.
    /// </para>
    /// </summary>
    public interface IStreamingEventProducer : IEventProducer
    {
        /// <summary>
        /// Generate an evnt stream that can be used to publish to the <see cref="IStreamingEventBus"/>
        /// </summary>
        /// <param name="topic"></param>
        /// <returns></returns>
        IEventStream CreateStream(string topic);

        /// <summary>
        /// Enumerates on the dataSet and publishes the elements to the bus.
        /// <para>
        /// In addition 2 new headers will be added to each event that is published:
        /// <list type="number">
        ///   <item>
        ///     <term>cmf.bus.message.patterns#streaming.sequenceId</term>
        ///     <description>A Guid that ties this event to a particular sequence. this is the unique identifier that
        ///     indicates the message is part of a larger data set.
        ///     </description>
        ///   </item>
        ///   <item>
        ///     <term>cmf.bus.message.patterns#streaming.position</term>
        ///     <description>An integer that indicates what position in the sequence this is.</description>
        ///   </item>
        /// </para>
        /// </summary>
        /// <typeparam name="TEvent"></typeparam>
        /// <param name="dataSet"></param>
        void PublishChunkedSequence<TEvent>(ICollection<TEvent> dataSet);

    }
}

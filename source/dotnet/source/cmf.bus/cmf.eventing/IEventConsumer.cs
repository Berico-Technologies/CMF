using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;

namespace cmf.eventing
{
    public interface IEventConsumer : IDisposable
    {
        void Subscribe(IEventHandler handler);

        /// <summary>
        /// Registers a handler via an Action that listens for a strongly typed event (TEvent) from the Bus.
        /// </summary>
        /// <typeparam name="TEvent">The specified type of event to listen for (e.g. - EntityQuery).</typeparam>
        /// <param name="handler">Action that listens for the TEvent type. The IDictionary received are the message headers sent with the event.</param>
        void Subscribe<TEvent>(Action<TEvent, IDictionary<string, string>> handler) where TEvent : class;
    }
}

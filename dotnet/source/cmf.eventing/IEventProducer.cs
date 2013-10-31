using System;
using System.Collections.Generic;
using cmf.bus;

namespace cmf.eventing
{
	/// <summary>
    /// An interface defining the methods by which an client may publish events.
	/// </summary>
    /// <seealso cref="IEventBus"/>
    public interface IEventProducer : IDisposable
    {
		/// <summary>
        /// Publishes an event.
		/// </summary>
		/// <param name="ev">The event to publish</param>
		/// <exception cref="System.Exception">May throw an exception.</exception>
        void Publish(object ev);
        //TODO: Consider moving Publish(object ev) to an extension method as per the pattern in IEventConsumer.

		/// <summary>
        /// Publishes an event and specifies a set of custom headers to send with it. 
		/// </summary>
		/// <param name="ev">The event to publish</param>
        /// <param name="headers">The custom headers to publish with it.</param>
		/// <exception cref="System.Exception">May throw an exception.</exception>
        /// <remarks>Normally the headers to be sent with an event are computed by the 
        /// IEventProducer implementation. If custom headers are provided, how they 
        /// are combined with any computed headers is implementation dependent.</remarks>
        /// <seealso cref="Envelope.Headers"/>
		void Publish(object ev, IDictionary<string, string> headers);
    }
}

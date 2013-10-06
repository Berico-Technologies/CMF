using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing
{
	/// <summary>
	/// The interface for a component that can produce events.
	/// </summary>
    public interface IEventProducer : IDisposable
    {
		/// <summary>
		/// Publish the specified event.
		/// </summary>
		/// <param name="ev">The event to publish</param>
		/// <exception cref="System.Exception">May throw an exception.</exception>
        void Publish(object ev);

		/// <summary>
		/// Publish the specified event with the given headers.
		/// </summary>
		/// <param name="ev">The event to publish</param>
		/// <param name="headers">The headers to add to the event.</param>
		/// <exception cref="System.Exception">May throw an exception.</exception>
		void Publish(object ev, IDictionary<string, string> headers);
    }
}

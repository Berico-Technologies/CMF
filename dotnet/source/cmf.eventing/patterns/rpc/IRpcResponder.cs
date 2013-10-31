using System;
using System.Collections.Generic;

namespace cmf.eventing.patterns.rpc
{
    /// <summary>
    /// An interface defining the methods by which a client responds to RPC calls.
    /// </summary>
    /// <remarks>Typically an instance of this interface used by implementations of 
    /// <see cref="IEventHandler"/> that handle RPC request in order that they
    /// may send responses to those requests.</remarks>
    /// <seealso cref="IRpcEventBus"/>
    /// <seealso cref="IRpcRequestor"/>
    public interface IRpcResponder : IDisposable
    {
        /// <summary>
        /// Sends a response to a specific RPC request.
        /// </summary>
        /// <param name="headers">The headers from the received event that contained the RPC 
	    /// request. (Used to route the response back to the appropriate caller.)</param>
        /// <param name="response">The response to sent.</param>
        /// <remarks>This method should be 
        /// called from <see cref="IEventHandler.Handle"/> in the course of
	    /// handling an RPC type event.</remarks>
        void RespondTo(IDictionary<string, string> headers, object response);
    }
}

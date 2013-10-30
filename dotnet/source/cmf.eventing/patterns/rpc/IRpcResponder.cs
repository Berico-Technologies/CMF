using System;
using System.Collections.Generic;

namespace cmf.eventing.patterns.rpc
{
    /// <summary>
    /// An interface defining the methods by which a client responds to RPC calls.
    /// </summary>
    public interface IRpcResponder : IDisposable
    {
        /// <summary>
        /// Sends a response to a specific RPC request.
        /// </summary>
        /// <param name="headers">The headers from the received event that contained the RPC 
	    /// request. (Used to route the response back to the appropriate caller.)</param>
        /// <param name="response">The response to sent.</param>
        void RespondTo(IDictionary<string, string> headers, object response);
    }
}

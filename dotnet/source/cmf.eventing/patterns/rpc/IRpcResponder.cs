using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing.patterns.rpc
{
    public interface IRpcResponder : IDisposable
    {
        /// <summary>
        /// Responds to an event.
        /// </summary>
        /// <param name="headers">The headers of the request to which we are responding.</param>
        /// <param name="response">The response to publish.</param>
        void RespondTo(IDictionary<string, string> headers, object response);
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.berico
{
    /// <summary>
    /// Defines the interface of a component that can process envelopes on 
    /// their way to and from the client.
    /// <remarks>
    /// If processing of the envelope should continue, call the provided
    /// continuation method.  If processing of the envelope should stop,
    /// do not call the continuation method.  Note that the caller is not
    /// notified that their envelope was not delivered in this case.  Finally,
    /// if an anomalous condition arises, throw an exception and it will 
    /// bubble up to the caller.
    /// </remarks>
    /// </summary>
    public interface IEnvelopeProcessor
    {
        void ProcessEnvelope(EnvelopeContext context, Action<EnvelopeContext> continueProcessing);
    }
}

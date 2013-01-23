using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;


namespace cmf.eventing.berico
{
    public interface IOutboundEventProcessor
    {
        void ProcessOutbound(ref object ev, ref Envelope env, IDictionary<string, object> context);
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;

namespace cmf.eventing.berico
{
    public interface IInboundEventProcessor
    {
        bool ProcessInbound(ref object ev, ref Envelope env, ref IDictionary<string, object> context);
    }
}

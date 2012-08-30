using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.berico
{
    public interface IInboundEnvelopeProcessor
    {
        void ProcessInbound(ref Envelope envelope, ref IDictionary<string, object> context);
    }
}

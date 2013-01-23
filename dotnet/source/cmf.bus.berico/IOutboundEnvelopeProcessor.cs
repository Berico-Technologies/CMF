using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.berico
{
    public interface IOutboundEnvelopeProcessor
    {
        void ProcessOutbound(ref Envelope envelope, ref IDictionary<string, object> context);
    }
}

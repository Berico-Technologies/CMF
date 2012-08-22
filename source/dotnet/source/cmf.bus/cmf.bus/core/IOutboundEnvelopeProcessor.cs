using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.core
{
    public interface IOutboundEnvelopeProcessor
    {
        void ProcessOutbound(Envelope envelope, IDictionary<string, object> context);
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.core
{
    public interface IInboundEnvelopeProcessor
    {
        void ProcessInbound(Envelope envelope, IDictionary<string, object> context);
    }
}

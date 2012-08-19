using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus.core;

namespace cmf.bus.transport
{
    public interface IInboundTransporter
    {
        event Action<Envelope> EnvelopeReceived;


        void RegisterFor(string topic);
    }
}

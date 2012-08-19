using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus.core;

namespace cmf.bus.transport
{
    public interface IOutboundTransporter
    {
        void Send(Envelope envelope);
    }
}

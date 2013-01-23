using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus
{
    public interface IEnvelopeBus : IEnvelopeSender, IEnvelopeReceiver, IDisposable
    {
    }
}

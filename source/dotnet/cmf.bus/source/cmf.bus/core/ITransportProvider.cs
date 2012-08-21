using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.core
{
    public interface ITransportProvider
    {
        event Action<Envelope> OnEnvelopeReceived;


        void Send(Envelope env);

        void Register(string topic);
    }
}

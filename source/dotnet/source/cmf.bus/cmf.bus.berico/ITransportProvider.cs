using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.berico
{
    public interface ITransportProvider : IDisposable
    {
        event Action<IEnvelopeDispatcher> OnEnvelopeReceived;


        void Send(Envelope env);

        void Register(IRegistration registration);

        void Unregister(IRegistration registration);
    }
}

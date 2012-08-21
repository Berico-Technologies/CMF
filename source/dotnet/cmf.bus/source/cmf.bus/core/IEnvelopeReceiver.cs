using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.core
{
    public interface IEnvelopeReceiver
    {
        event Action<Envelope, Exception> OnFailedEnvelope;


        void Register(IRegistration registration);

        void Register(string topic, Func<Envelope, DeliveryOutcome> handler);
    }
}

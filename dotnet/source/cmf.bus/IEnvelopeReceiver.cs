using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus
{
    public interface IEnvelopeReceiver
    {
        void Register(IRegistration registration);

        void Unregister(IRegistration registration);
    }
}

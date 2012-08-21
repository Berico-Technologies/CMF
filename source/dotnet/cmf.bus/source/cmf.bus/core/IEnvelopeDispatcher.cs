using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.core
{
    public interface IEnvelopeDispatcher
    {
        void Dispatch(Envelope env, IEnumerable<IRegistration> registeredHandlers);
    }
}

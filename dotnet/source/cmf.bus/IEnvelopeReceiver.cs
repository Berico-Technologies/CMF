using System;

namespace cmf.bus
{
    public interface IEnvelopeReceiver : IDisposable
    {
        void Register(IRegistration registration);

        void Unregister(IRegistration registration);
    }
}

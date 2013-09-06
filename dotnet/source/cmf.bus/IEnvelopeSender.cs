using System;

namespace cmf.bus
{
    public interface IEnvelopeSender : IDisposable
    {
        void Send(Envelope env);
    }
}

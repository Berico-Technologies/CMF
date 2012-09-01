using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.berico
{
    public interface IEnvelopeDispatcher
    {
        Envelope Envelope { get; }


        void Dispatch();

        void Dispatch(Envelope env);

        void Fail(Exception ex);
    }
}

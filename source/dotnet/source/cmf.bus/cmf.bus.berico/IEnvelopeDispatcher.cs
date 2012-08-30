using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.berico
{
    public interface IEnvelopeDispatcher
    {
        Envelope Envelope { get; }


        object Dispatch();

        object Dispatch(Envelope env);

        object Fail(Exception ex);
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.core
{
    public interface IEnvelopeSender
    {
        void Send(Envelope env);
    }
}

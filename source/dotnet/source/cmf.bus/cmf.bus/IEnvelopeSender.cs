using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus
{
    public interface IEnvelopeSender
    {
        void Send(Envelope env);
    }
}

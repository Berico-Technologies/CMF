using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.core
{
    public interface IEnvelopeProcessor
    {
        void Process(Envelope envelope, IDictionary<string, object> context);
    }
}

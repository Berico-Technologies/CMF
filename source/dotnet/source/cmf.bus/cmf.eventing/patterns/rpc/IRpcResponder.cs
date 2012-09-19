using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing.patterns.rpc
{
    public interface IRpcResponder
    {
        void RespondTo(IDictionary<string, string> headers, object response);
    }
}

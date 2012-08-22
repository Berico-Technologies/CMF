using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.eventing.patterns
{
    public interface IRpcResponder
    {
        void RespondTo(Guid requestId, object response);
    }
}

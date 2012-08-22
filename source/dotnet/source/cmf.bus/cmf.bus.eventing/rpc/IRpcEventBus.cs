using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus.eventing.core;

namespace cmf.bus.eventing.patterns
{
    public interface IRpcEventBus : IEventBus, IRpcRequestor, IRpcResponder
    {
    }
}

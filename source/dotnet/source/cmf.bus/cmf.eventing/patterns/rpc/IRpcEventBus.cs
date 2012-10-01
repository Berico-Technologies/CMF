using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.eventing;

namespace cmf.eventing.patterns.rpc
{
    public interface IRpcEventBus : IEventBus, IRpcRequestor, IRpcResponder, IDisposable
    {
    }
}

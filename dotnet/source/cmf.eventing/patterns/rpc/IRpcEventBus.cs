using System;

namespace cmf.eventing.patterns.rpc
{
    /// <summary>
    /// A convenience interface that combines the <see cref="IEventBus" />, 
    /// <see cref="IRpcRequestor" /> and <see cref="IRpcResponder" /> 
    /// interfaces together because they are most typically implemented 
    /// and referenced together when performing RPC type eventing.
    /// </summary>
    public interface IRpcEventBus : IEventBus, IRpcRequestor, IRpcResponder
    {
    }
}

using System;

namespace cmf.eventing.patterns.rpc
{
    /// <summary>
    /// A convenience interface that combines the <see cref="IEventBus">IEventBus</see>, 
    /// <see cref="IRpcRequestor">IRpcRequestor</see> and <see cref="IRpcResponder">IRpcResponder</see> 
    /// interfaces together because they are most typically implemented 
    /// and referenced together when performing RPC type eventing.
    /// </summary>
    public interface IRpcEventBus : IEventBus, IRpcRequestor, IRpcResponder
    {
    }
}

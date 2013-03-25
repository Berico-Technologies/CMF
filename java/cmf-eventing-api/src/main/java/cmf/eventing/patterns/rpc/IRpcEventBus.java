package cmf.eventing.patterns.rpc;

import cmf.bus.IDisposable;
import cmf.eventing.IEventBus;

/**
 * A convenience interface that combines the {@link IEventBus }, {@link IRpcSender} 
 * and {@link IRpcReceiver} interfaces together because they are most typically
 * implemented and referenced together when performing RPC type eventing.
 */
public interface IRpcEventBus extends IEventBus, IRpcSender, IRpcReceiver, IDisposable {

}

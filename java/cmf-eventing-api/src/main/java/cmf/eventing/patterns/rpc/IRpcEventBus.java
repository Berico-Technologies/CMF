package cmf.eventing.patterns.rpc;

import cmf.bus.IDisposable;
import cmf.eventing.IEventBus;

public interface IRpcEventBus extends IEventBus, IRpcSender, IRpcReceiver, IDisposable {

}

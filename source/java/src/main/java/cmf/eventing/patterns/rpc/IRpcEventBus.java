package cmf.eventing.patterns.rpc;

import cmf.eventing.IEventBus;

public interface IRpcEventBus extends IEventBus, IRpcSender, IRpcReceiver {

}

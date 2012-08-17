package cmf.bus.service.module;

import java.util.Collection;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.IReceiveModule;

public class ReceiveModuleCollection implements IReceiveModule {

    private Collection<IReceiveModule> receiveModuleCollection;

    public ReceiveModuleCollection() {

    }

    public void setReceiveModule(Collection<IReceiveModule> receiveModuleCollection) {
        this.receiveModuleCollection = receiveModuleCollection;
    }

    @Override
    public void receive(IEnvelope envelope) {
        for (IReceiveModule receiveModule : receiveModuleCollection) {
            receiveModule.receive(envelope);
        }
    }

}

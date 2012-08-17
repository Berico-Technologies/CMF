package cmf.bus.service.module;

import java.util.Collection;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.ISendModule;

public class SendModuleCollection implements ISendModule {

    private Collection<ISendModule> sendModuleCollection;

    public SendModuleCollection() {

    }

    public void setSendModule(Collection<ISendModule> sendModuleCollection) {
        this.sendModuleCollection = sendModuleCollection;
    }

    @Override
    public void send(IEnvelope envelope) {
        for (ISendModule sendModule : sendModuleCollection) {
            sendModule.send(envelope);
        }
    }

}

package cmf.bus.core.module;

import cmf.bus.core.IEnvelope;

public interface ISendModule {

    void processEnvelope(IEnvelope envelope);

}

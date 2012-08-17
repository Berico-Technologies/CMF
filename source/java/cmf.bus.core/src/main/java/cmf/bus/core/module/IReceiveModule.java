package cmf.bus.core.module;

import cmf.bus.core.IEnvelope;

public interface IReceiveModule {

    void processEnvelope(IEnvelope envelope);

}

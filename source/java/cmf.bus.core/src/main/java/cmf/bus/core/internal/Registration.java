package cmf.bus.core.internal;

import cmf.bus.core.IEnvelopeHandler;
import cmf.bus.core.IRegistration;
import cmf.bus.core.ITransportFilter;

public class Registration implements IRegistration {

    protected IEnvelopeHandler envelopeHandler;
    protected ITransportFilter transportFilter;

    @Override
    public IEnvelopeHandler getEnvelopeHandler() {
        return envelopeHandler;
    }

    @Override
    public ITransportFilter getTransportFilter() {
        return transportFilter;
    }

    @Override
    public void setEnvelopeHandler(IEnvelopeHandler envelopeHandler) {
        this.envelopeHandler = envelopeHandler;
    }

    @Override
    public void setTransportFilter(ITransportFilter transportFilter) {
        this.transportFilter = transportFilter;
    }

}

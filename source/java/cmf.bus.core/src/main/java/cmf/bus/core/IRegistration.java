package cmf.bus.core;

public interface IRegistration {

    IEnvelopeHandler getEnvelopeHandler();

    ITransportFilter getTransportFilter();

    void setEnvelopeHandler(IEnvelopeHandler envelopeHandler);

    void setTransportFilter(ITransportFilter transportFilter);

}

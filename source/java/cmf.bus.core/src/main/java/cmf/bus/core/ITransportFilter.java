package cmf.bus.core;

public interface ITransportFilter {

    boolean filter(IEnvelope envelope);

}

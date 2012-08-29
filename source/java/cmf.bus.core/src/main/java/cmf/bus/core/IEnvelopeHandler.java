package cmf.bus.core;

public interface IEnvelopeHandler {

    DeliveryOutcome handleEnvelope(IEnvelope envelope);

}

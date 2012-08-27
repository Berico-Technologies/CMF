package cmf.bus.core;

public interface IEnvelopeHandler {

    DeliveryOutcome receive(IEnvelope envelope);

}

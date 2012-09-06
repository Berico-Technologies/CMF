package cmf.bus.core;

public interface IEnvelopeHandler {

    DeliveryOutcome handleEnvelope(Envelope envelope);

}

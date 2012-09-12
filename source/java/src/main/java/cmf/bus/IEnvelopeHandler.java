package cmf.bus;

public interface IEnvelopeHandler {

    Object handle(Envelope envelope);

    Object handleFailed(Envelope envelope, Exception e);
}

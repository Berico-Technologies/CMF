package cmf.bus;

public interface IEnvelopeHandler {

    Object handle(Envelope envelope) throws Exception;

    Object handleFailed(Envelope envelope, Exception e);
}

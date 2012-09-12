package cmf.bus.berico;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeHandler;

public class NullEnvelopeHandler implements IEnvelopeHandler {

    @Override
    public Object handle(Envelope envelope) {
        return null;
    }

    @Override
    public Object handleFailed(Envelope envelope, Exception e) {
        return null;
    }
}

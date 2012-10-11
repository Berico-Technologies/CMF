package cmf.bus.berico;

import org.apache.commons.lang.time.StopWatch;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeHandler;
import cmf.bus.berico.exception.TimeoutException;

public class BlockingEnvelopeHandler implements IEnvelopeHandler {

    private Envelope envelope;
    protected long timeoutMillis;

    public BlockingEnvelopeHandler(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    protected Envelope blockOn(Envelope envelope) {
        StopWatch watch = new StopWatch();
        watch.start();
        while (watch.getTime() < timeoutMillis && envelope == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException("BlockingEnvelopeHandler received an interupt exception.", e);
            }
        }
        watch.stop();
        if (envelope == null) {
            throw new TimeoutException("BlockingEnvelopeHandler timed out while waiting for an envelope.");
        }

        return envelope;
    }

    public Envelope getEnvelope() {
        return blockOn(envelope);
    }

    @Override
    public Object handle(Envelope envelope) {
        this.envelope = envelope;

        return null;
    }

    @Override
    public Object handleFailed(Envelope envelope, Exception e) {
        return null;
    }
}

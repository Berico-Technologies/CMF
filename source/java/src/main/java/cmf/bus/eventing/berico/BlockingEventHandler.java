package cmf.bus.eventing.berico;

import java.util.Map;

import org.apache.commons.lang.time.StopWatch;

import cmf.bus.Envelope;
import cmf.bus.berico.exception.TimeoutException;
import cmf.bus.eventing.IEventHandler;

public class BlockingEventHandler<TEVENT> implements IEventHandler<TEVENT> {

    private TEVENT event;
    private Map<String, String> headers;
    protected long timeoutMillis;

    public BlockingEventHandler(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    protected <BLOCK_ITEM> BLOCK_ITEM blockOn(BLOCK_ITEM item) {
        StopWatch watch = new StopWatch();
        watch.start();
        while (watch.getTime() < timeoutMillis && item == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException("BlockingEnvelopeHandler received an interupt exception.", e);
            }
        }
        watch.stop();
        if (item == null) {
            throw new TimeoutException("BlockingEnvelopeHandler timed out while waiting for an event.");
        }

        return item;
    }

    public TEVENT getEvent() {
        return blockOn(event);
    }

    @Override
    public Class<TEVENT> getEventType() {
        return null;
    }

    public Map<String, String> getHeaders() {
        return blockOn(headers);
    }

    @Override
    public Object handle(TEVENT event, Map<String, String> headers) {
        this.event = event;
        this.headers = headers;

        return null;
    }

    @Override
    public Object handleFailed(Envelope envelope, Exception e) {
        return null;
    }
}

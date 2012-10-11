package cmf.bus.berico;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cmf.bus.Envelope;
import cmf.bus.berico.exception.TimeoutException;

public class BlockingEnvelopeBusTest {

    private BlockingEnvelopeHandler handler;
    private long timeout = 50L;
    @Mock
    private Envelope envelope;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        handler = new BlockingEnvelopeHandler(timeout);
    }

    @Test(expected = TimeoutException.class)
    public void timeoutThrows() {
        handler.getEnvelope();
    }

    @Test
    public void handleSetsEnvelope() {
        handler.handle(envelope);
        assertEquals(envelope, handler.getEnvelope());
    }
}

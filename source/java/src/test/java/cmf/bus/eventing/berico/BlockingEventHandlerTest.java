package cmf.bus.eventing.berico;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cmf.bus.berico.exception.TimeoutException;
import cmf.eventing.berico.BlockingEventHandler;

public class BlockingEventHandlerTest {

    private BlockingEventHandler<Object> handler;
    private long timeout = 50L;
    @Mock
    private Object event;
    @Mock
    private Map<String, String> headers;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        handler = new BlockingEventHandler<Object>(timeout);
    }

    @Test(expected = TimeoutException.class)
    public void getEventTimeoutThrows() {
        handler.getEvent();
    }

    @Test(expected = TimeoutException.class)
    public void getHeadersTimeoutThrows() {
        handler.getHeaders();
    }

    @Test
    public void handleSetsEvent() {
        handler.handle(event, headers);
        assertEquals(event, handler.getEvent());
    }

    @Test
    public void handleSetsHeaders() {
        handler.handle(event, headers);
        assertEquals(headers, handler.getHeaders());
    }
}

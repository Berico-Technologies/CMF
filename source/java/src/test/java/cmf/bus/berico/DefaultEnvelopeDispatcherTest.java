package cmf.bus.berico;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IEnvelopeHandler;
import cmf.bus.IRegistration;

public class DefaultEnvelopeDispatcherTest {

    private DefaultEnvelopeDispatcher dispatcher;
    @Mock
    private Envelope envelope;
    @Mock
    private IEnvelopeFilterPredicate filterPredicate;
    @Mock
    private IEnvelopeHandler handler;
    @Mock
    private IRegistration registration;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        doAnswer(new Answer<IEnvelopeHandler>() {

            @Override
            public IEnvelopeHandler answer(InvocationOnMock invocation) throws Throwable {
                return handler;
            }
        }).when(registration).getHandler();

        doAnswer(new Answer<IEnvelopeFilterPredicate>() {

            @Override
            public IEnvelopeFilterPredicate answer(InvocationOnMock invocation) throws Throwable {
                return filterPredicate;
            }
        }).when(registration).getFilterPredicate();

        dispatcher = new DefaultEnvelopeDispatcher();
    }

    @Test
    public void dispatchCallsFilterPredicate() {
        dispatcher.dispatch(registration, envelope);
        verify(filterPredicate).filter(any(Envelope.class));
    }

    @Test
    public void dispatcherCallsHandler() {
        doAnswer(new Answer<Boolean>() {

            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        }).when(filterPredicate).filter(any(Envelope.class));
        dispatcher.dispatch(registration, envelope);
        verify(handler).handle(any(Envelope.class));
    }

    @Test
    public void dispatchFailedCallsHandlerFailed() {
        dispatcher.dispatchFailed(registration, envelope, null);
        verify(handler).handleFailed(any(Envelope.class), any(Exception.class));
    }

    @Test
    public void filterFalseDoesntCallHandler() {
        doAnswer(new Answer<Boolean>() {

            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return false;
            }
        }).when(filterPredicate).filter(any(Envelope.class));
        dispatcher.dispatch(registration, envelope);
        verify(handler, never()).handle(any(Envelope.class));
    }
}

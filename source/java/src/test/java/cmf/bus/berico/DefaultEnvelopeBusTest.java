package cmf.bus.berico;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeHandler;
import cmf.bus.IRegistration;

@SuppressWarnings("unchecked")
public class DefaultEnvelopeBusTest {

    @Mock
    private Map<String, Object> context;
    @Mock
    private Envelope envelope;
    private DefaultEnvelopeBus envelopeBus;
    @Mock
    private IInboundEnvelopeProcessor inboundEnvelopeProcessor;
    private List<IInboundEnvelopeProcessor> inboundProcessors = new LinkedList<IInboundEnvelopeProcessor>();
    @Mock
    private IOutboundEnvelopeProcessor outboundEnvelopeProcessor;
    private List<IOutboundEnvelopeProcessor> outboundProcessors = new LinkedList<IOutboundEnvelopeProcessor>();
    @Mock
    private IRegistration registration;
    @Mock
    private ITransportProvider transportProvider;
    private IRegistration userRegistration;
    @Mock
    private IEnvelopeHandler envelopeHandler;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        inboundProcessors.add(inboundEnvelopeProcessor);
        outboundProcessors.add(outboundEnvelopeProcessor);
        envelopeBus = new DefaultEnvelopeBus(transportProvider, inboundProcessors, outboundProcessors);
    }

    @Test
    public void registerCallsTransportProviderRegister() {
        envelopeBus.register(registration);
        verify(transportProvider).register(any(IRegistration.class));
    }

    @Test(expected = RuntimeException.class)
    public void registerThrowsOnNullRegistration() {
        envelopeBus.register((IRegistration) null);
    }

    @Test
    public void sendCallsOutboundProcessor() {
        envelopeBus.send(envelope);
        verify(outboundEnvelopeProcessor).processOutbound(any(Envelope.class), anyMap());
    }

    @Test
    public void sendCallsTransportProviderSend() {
        envelopeBus.send(envelope);
        verify(transportProvider).send(any(Envelope.class));
    }

    @Test(expected = RuntimeException.class)
    public void sendThrowsOnNullEnvelope() {
        envelopeBus.send((Envelope) null);
    }

    @Test
    public void receiveCallsInboundProcessor() {
        // capture the registration wrapper object
        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                userRegistration = (IRegistration) invocation.getArguments()[0];

                return null;
            }
        }).when(transportProvider).register(any(IRegistration.class));
        doAnswer(new Answer<IEnvelopeHandler>() {

            @Override
            public IEnvelopeHandler answer(InvocationOnMock invocation) throws Throwable {
                return envelopeHandler;
            }
        }).when(registration).getHandler();
        envelopeBus.register(registration);
        IEnvelopeHandler handler = userRegistration.getHandler();
        handler.handle(envelope);
        verify(inboundEnvelopeProcessor).processInbound(any(Envelope.class), anyMap());
    }
}

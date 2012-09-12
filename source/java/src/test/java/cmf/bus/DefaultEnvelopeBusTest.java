//package cmf.bus;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.doAnswer;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//
//import java.util.Collection;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//
//import cmf.bus.DefaultEnvelopeBus;
//import cmf.bus.Envelope;
//import cmf.bus.IInboundEnvelopeProcessor;
//import cmf.bus.IOutboundEnvelopeProcessor;
//import cmf.bus.IRegistration;
//import cmf.bus.ITransportProvider;
//
//@SuppressWarnings("unchecked")
//public class DefaultEnvelopeBusTest {
//
//    @Mock
//    private Map<String, Object> context;
//    @Mock
//    private Envelope envelope;
//    private DefaultEnvelopeBus defaultEnvelopeBus;
//    @Mock
//    private IInboundEnvelopeProcessor inboundEnvelopeProcessor;
//    private List<IInboundEnvelopeProcessor> inboundProcessors = new LinkedList<IInboundEnvelopeProcessor>();
//    @Mock
//    private IOutboundEnvelopeProcessor outboundEnvelopeProcessor;
//    private List<IOutboundEnvelopeProcessor> outboundProcessors = new LinkedList<IOutboundEnvelopeProcessor>();
//    @Mock
//    private IRegistration registration;
//    @Mock
//    private ITransportProvider transportProvider;
//
//    @Before
//    public void before() {
//        MockitoAnnotations.initMocks(this);
//
//        doAnswer(new Answer<Object>() {
//
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                return null;
//            }
//        }).when(registration).handle(envelope);
//        
//        inboundProcessors.add(inboundEnvelopeProcessor);
//        outboundProcessors.add(outboundEnvelopeProcessor);
//        defaultEnvelopeBus = new DefaultEnvelopeBus();
//        defaultEnvelopeBus.setInboundProcessors(inboundProcessors);
//        defaultEnvelopeBus.setOutboundProcessorCollection(outboundProcessors);
//        defaultEnvelopeBus.setTransportProvider(transportProvider);
//    }
//
//    @Test
//    public void inboundExceptionDoesntThrow() {
//        doThrow(RuntimeException.class).when(inboundEnvelopeProcessor).processInbound(any(Envelope.class), any(Map.class));
//        defaultEnvelopeBus.register(registration);
//        registration.
//        envelopeBusRegistrationEnvelopeHandler.handleEnvelope(envelope);
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void outboundExceptionThrows() {
//        doThrow(RuntimeException.class).when(outboundEnvelopeProcessor).processOutbound(any(Envelope.class), any(Map.class));
//        defaultEnvelopeBus.send(envelope);
//    }
//
//    @Test
//    public void receiveCallsProcessors() {
//        defaultEnvelopeBus.register(registration);
//        envelopeBusRegistrationEnvelopeHandler.handleEnvelope(envelope);
//        verify(inboundEnvelopeProcessor).processInbound(any(Envelope.class), any(Map.class));
//    }
//
//    @Test
//    public void registerCallsTransportRegister() {
//        defaultEnvelopeBus.register(registration);
//        verify(transportProvider).register(any(IRegistration.class));
//    }
//
//    @Test
//    public void sendCallsProcessors() {
//        defaultEnvelopeBus.send(envelope);
//        verify(outboundEnvelopeProcessor).processOutbound(any(Envelope.class), any(Map.class));
//    }
//
//    @Test
//    public void sendCallsTransportSend() {
//        defaultEnvelopeBus.send(envelope);
//        verify(transportProvider).send(any(Envelope.class));
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void transportRegisterExceptionThrows() {
//        doThrow(RuntimeException.class).when(transportProvider).register(any(IRegistration.class));
//        defaultEnvelopeBus.register(registration);
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void transportSendExceptionThrows() {
//        doThrow(RuntimeException.class).when(transportProvider).send(any(Envelope.class));
//        defaultEnvelopeBus.send(envelope);
//    }
//
//}

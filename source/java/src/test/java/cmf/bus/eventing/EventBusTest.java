package cmf.bus.eventing;
//package cmf.bus.eventing;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.doAnswer;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//
//import cmf.bus.Envelope;
//import cmf.bus.IEnvelopeBus;
//import cmf.bus.IRegistration;
//import cmf.bus.ISerializer;
//import cmf.bus.berico.event.EventBus;
//import cmf.bus.berico.event.IEventHandler;
//
//public class EventBusTest {
//
//    @Mock
//    private Envelope envelope;
//    @Mock
//    private IEnvelopeBus envelopeBus;
//    private EventBus eventBus;
//    @Mock
//    private IRegistration registration;
//    @Mock
//    private IEventHandler<Object> eventHandler;
//    @Mock
//    private ISerializer serializer;
//
//    @Before
//    public void before() {
//        MockitoAnnotations.initMocks(this);
//
//        doAnswer(new Answer<byte[]>() {
//
//            @Override
//            public byte[] answer(InvocationOnMock invocation) throws Throwable {
//                byte[] serialized = {};
//                
//                return serialized;
//            }
//            
//        }).when(serializer).byteSerialize(any(Object.class));
//        eventBus = new EventBus();
//        eventBus.setEnvelopeBus(envelopeBus);
//        eventBus.setSerializer(serializer);
//    }
//    
//    @Test
//    public void registerCallsEnvelopeBusRegister() {
//        eventBus.register(eventHandler, Object.class);
//        verify(envelopeBus).register(any(IRegistration.class));
//    }
//
//    @Test
//    public void sendCallsEnvelopeBusSend() {
//        eventBus.send(new Object());
//        verify(envelopeBus).send(any(Envelope.class));
//    }
//    
//    @Test(expected = RuntimeException.class)
//    public void envelopeBusSendExceptionThrows() {
//        doThrow(RuntimeException.class).when(envelopeBus).send(any(Envelope.class));
//        eventBus.send(envelope);
//    }
//
//    @Test(expected = RuntimeException.class)
//    public void envelopeBusRegisterExceptionThrows() {
//        doThrow(RuntimeException.class).when(envelopeBus).register(any(IRegistration.class));
//        eventBus.register(eventHandler, Object.class);
//    }
//
//}

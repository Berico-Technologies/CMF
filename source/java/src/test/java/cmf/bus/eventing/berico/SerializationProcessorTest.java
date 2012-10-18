//package cmf.bus.eventing.berico;
//
//import static org.mockito.Mockito.doAnswer;
//import static org.mockito.Mockito.eq;
//import static org.mockito.Mockito.verify;
//
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//
//import cmf.bus.Envelope;
//import cmf.eventing.berico.ISerializer;
//import cmf.eventing.berico.SerializationProcessor;
//import cmf.rabbit.support.RabbitEnvelopeHelper;
//
//public class SerializationProcessorTest {
//
//    @Mock
//    private ISerializer serializer;
//    @Mock
//    private Object event;
//    @Mock
//    private Envelope envelope;
//    @Mock
//    private Map<String, Object> context;
//    private byte[] payload = new byte[] { 0, 0, 1, 1 };
//    private Class<?> type = String.class;
//
//    private SerializationProcessor processor;
//
//    @Before
//    public void before() {
//        MockitoAnnotations.initMocks(this);
//
//        processor = new SerializationProcessor(serializer);
//    }
//
//    @Test
//    public void processInboundCallsDeserialize() {
//        doAnswer(new Answer<byte[]>() {
//
//            @Override
//            public byte[] answer(InvocationOnMock invocation) throws Throwable {
//                return payload;
//            }
//        }).when(envelope).getPayload();
//        doAnswer(new Answer<String>() {
//
//            @Override
//            public String answer(InvocationOnMock invocation) throws Throwable {
//                return type.getCanonicalName();
//            }
//        }).when(envelope).getHeader(RabbitEnvelopeHelper.Headers.TYPE);
//        processor.processInbound(event, envelope, context);
//        verify(serializer).byteDeserialize(eq(payload), eq(type));
//    }
//
//    @Test
//    public void processOutboundCallsSerialize() {
//        processor.processOutbound(event, envelope, context);
//        verify(serializer).byteSerialize(eq(event));
//    }
//
//    @Test
//    public void processOutboundCallsSetPayload() {
//        doAnswer(new Answer<byte[]>() {
//
//            @Override
//            public byte[] answer(InvocationOnMock invocation) throws Throwable {
//                return payload;
//            }
//        }).when(serializer).byteSerialize(event);
//        processor.processOutbound(event, envelope, context);
//        verify(envelope).setPayload(eq(payload));
//    }
//
//    @Test
//    public void processOutboundSetsEventType() {
//        doAnswer(new Answer<String>() {
//
//            @Override
//            public String answer(InvocationOnMock invocation) throws Throwable {
//                return type.getCanonicalName();
//            }
//        }).when(envelope).getHeader(RabbitEnvelopeHelper.Headers.TYPE);
//        processor.processOutbound(event, envelope, context);
//        verify(envelope).setHeader(eq(RabbitEnvelopeHelper.Headers.TYPE), eq(event.getClass().getCanonicalName()));
//    }
//}

//package cmf.bus.berico.rabbit;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Matchers.eq;
//import static org.mockito.Mockito.doAnswer;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//
//import com.rabbitmq.client.BasicProperties;
//import com.rabbitmq.client.Channel;
//
//import cmf.bus.Envelope;
//import cmf.bus.IRegistration;
//import cmf.bus.berico.IEnvelopeReceivedCallback;
//
//@SuppressWarnings("serial")
//public class QueueTest {
//
//    @Mock
//    private Channel channel;
//    @Mock
//    private IRegistration registration;
//    @Mock
//    private com.rabbitmq.client.Envelope rabbitEnvelope;
//    @Mock
//    private IEnvelopeReceivedCallback callback;
//    @Mock
//    private BasicProperties properties;
//    private byte[] body = new byte[] { 1, 1, 0, 0 };
//
//    private String queueName = "queueName";
//    private String consumerTag = "consumerTag";
//    private String exchangeName = "exchangeName";
//    private String routingKey = "routingKey";
//
//    private Queue queue;
//
//    @Before
//    public void before() {
//        MockitoAnnotations.initMocks(this);
//
//        queue = new Queue(channel, callback, queueName, consumerTag);
//    }
//
//    @Test
//    public void bindCallsRabbitBind() throws IOException {
//        queue.bind(exchangeName, routingKey);
//        verify(channel).queueBind(anyString(), eq(exchangeName), eq(routingKey));
//    }
//
//    @Test
//    public void bindCallsRabbitConsume() throws IOException {
//        queue.bind(exchangeName, routingKey);
//        verify(channel).basicConsume(anyString(), eq(false), eq(consumerTag), eq(queue));
//    }
//
//    @Test
//    public void handleDeliveryGetsDeliveryTag() {
//        queue.handleDelivery(consumerTag, rabbitEnvelope, properties, body);
//        verify(rabbitEnvelope).getDeliveryTag();
//    }
//
//    @Test
//    public void handleDeliveryCallsCallback() {
//        queue.handleDelivery(consumerTag, rabbitEnvelope, properties, body);
//        verify(callback).handleReceive(any(Envelope.class));
//    }
//
//    @Test
//    public void handleDeliverySetsHeaders() {
//        final String key1 = "key1";
//        final String value1 = "value1";
//        final String key2 = "key2";
//        final String value2 = "value2";
//        final Map<String, Object> bpHeaders = new HashMap<String, Object>() {
//
//            {
//                put(key1, value1);
//                put(key2, value2);
//            }
//        };
//        doAnswer(new Answer<Map<String, Object>>() {
//
//            @Override
//            public Map<String, Object> answer(InvocationOnMock invocation) throws Throwable {
//                return bpHeaders;
//            }
//        }).when(properties).getHeaders();
//        queue.handleDelivery(consumerTag, rabbitEnvelope, properties, body);
//        
//        ArgumentCaptor<Envelope> envelopeArgumentCaptor = ArgumentCaptor.forClass(Envelope.class);
//        verify(callback).handleReceive(envelopeArgumentCaptor.capture());
//        Envelope envelope = envelopeArgumentCaptor.getValue();
//        
//        assertEquals(value1, envelope.getHeader(key1));
//        assertEquals(value2, envelope.getHeader(key2));
//        assertEquals(bpHeaders.size(), envelope.getHeaders().size());
//    }
//
//    @Test
//    public void nullResultCallsAckSucess() throws IOException {
//        final Object result = null;
//        doAnswer(new Answer<Object>() {
//
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                return result;
//            }
//        }).when(callback).handleReceive(any(Envelope.class));
//        queue.handleDelivery(consumerTag, rabbitEnvelope, properties, body);
//        verify(channel).basicAck(any(Long.class), eq(false));
//    }
//
//    @Test
//    public void acknowledgeResultCallsAckSucess() throws IOException {
//        final Object result = DeliveryOutcomes.Acknowledge;
//        doAnswer(new Answer<Object>() {
//
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                return result;
//            }
//        }).when(callback).handleReceive(any(Envelope.class));
//        queue.handleDelivery(consumerTag, rabbitEnvelope, properties, body);
//        verify(channel).basicAck(any(Long.class), eq(false));
//    }
//
//    @Test
//    public void rejectResultCallsAckRetry() throws IOException {
//        final Object result = DeliveryOutcomes.Reject;
//        doAnswer(new Answer<Object>() {
//
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                return result;
//            }
//        }).when(callback).handleReceive(any(Envelope.class));
//        queue.handleDelivery(consumerTag, rabbitEnvelope, properties, body);
//        verify(channel).basicAck(any(Long.class), eq(true));
//    }
//
//    @Test
//    public void exceptionResultCallsAckFailure() throws IOException {
//        final Object result = DeliveryOutcomes.Exception;
//        doAnswer(new Answer<Object>() {
//
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                return result;
//            }
//        }).when(callback).handleReceive(any(Envelope.class));
//        queue.handleDelivery(consumerTag, rabbitEnvelope, properties, body);
//        verify(channel).basicNack(any(Long.class), eq(false), eq(false));
//    }
//
//    @Test
//    public void actualExceptionResultCallsAckFailure() throws IOException {
//        doThrow(RuntimeException.class).when(callback).handleReceive(any(Envelope.class));
//        queue.handleDelivery(consumerTag, rabbitEnvelope, properties, body);
//        verify(channel).basicNack(any(Long.class), eq(false), eq(false));
//    }
//
//    @Test
//    public void stopCallsCancel() throws IOException {
//        queue.stop();
//        verify(channel).basicCancel(eq(consumerTag));
//    }
//
//    @Test
//    public void stopCallsChannelClose() throws IOException {
//        queue.stop();
//        verify(channel).close();
//    }
//}

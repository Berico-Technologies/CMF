package cmf.bus.berico.rabbit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;
import cmf.bus.berico.IEnvelopeDispatcher;

@SuppressWarnings("serial")
public class QueueTest {

    @Mock
    private Channel channel;
    @Mock
    private IRegistration registration;
    @Mock
    private IEnvelopeDispatcher dispatcher;
    @Mock
    private com.rabbitmq.client.Envelope envelope;
    @Mock
    private BasicProperties properties;
    private byte[] body = new byte[] { 1, 1, 0, 0 };

    private String consumerTag = "consumerTag";
    private String exchangeName = "exchangeName";
    private String routingKey = "routingKey";

    private Queue queue;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        queue = new Queue(channel, registration, dispatcher, consumerTag);
    }

    @Test
    public void bindCallsRabbitBind() throws IOException {
        queue.bind(exchangeName, routingKey);
        verify(channel).queueBind(anyString(), anyString(), anyString());
    }

    @Test
    public void bindCallsRabbitConsume() throws IOException {
        queue.bind(exchangeName, routingKey);
        verify(channel).basicConsume(anyString(), anyBoolean(), anyString(), any(DefaultConsumer.class));
    }

    @Test
    public void handleDeliveryGetsDeliveryTag() {
        queue.handleDelivery(consumerTag, envelope, properties, body);
        verify(envelope).getDeliveryTag();
    }

    @Test
    public void handleDeliveryCallsDispatch() {
        queue.handleDelivery(consumerTag, envelope, properties, body);
        verify(dispatcher).dispatch(any(IRegistration.class), any(Envelope.class));
    }

    @Test
    public void handleDeliverySetsHeaders() {
        final String key1 = "key1";
        final String value1 = "value1";
        final String key2 = "key2";
        final String value2 = "value2";
        final Map<String, Object> bpHeaders = new HashMap<String, Object>() {

            {
                put(key1, value1);
                put(key2, value2);
            }
        };
        doAnswer(new Answer<Map<String, Object>>() {

            @Override
            public Map<String, Object> answer(InvocationOnMock invocation) throws Throwable {
                return bpHeaders;
            }
        }).when(properties).getHeaders();
        queue.handleDelivery(consumerTag, envelope, properties, body);
        ArgumentCaptor<Envelope> envelopeArgumentCaptor = ArgumentCaptor.forClass(Envelope.class);
        verify(dispatcher).dispatch(any(IRegistration.class), envelopeArgumentCaptor.capture());
        Envelope envelope = envelopeArgumentCaptor.getValue();
        assertEquals(value1, envelope.getHeader(key1));
        assertEquals(value2, envelope.getHeader(key2));
        assertEquals(bpHeaders.size(), envelope.getHeaders().size());
    }

    @Test
    public void nullResultCallsAckSucess() throws IOException {
        final Object result = null;
        doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return result;
            }
        }).when(dispatcher).dispatch(any(IRegistration.class), any(Envelope.class));
        queue.handleDelivery(consumerTag, envelope, properties, body);
        verify(channel).basicAck(any(Long.class), eq(false));
    }

    @Test
    public void acknowledgeResultCallsAckSucess() throws IOException {
        final Object result = DeliveryOutcome.Acknowledge;
        doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return result;
            }
        }).when(dispatcher).dispatch(any(IRegistration.class), any(Envelope.class));
        queue.handleDelivery(consumerTag, envelope, properties, body);
        verify(channel).basicAck(any(Long.class), eq(false));
    }

    @Test
    public void rejectResultCallsAckRetry() throws IOException {
        final Object result = DeliveryOutcome.Reject;
        doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return result;
            }
        }).when(dispatcher).dispatch(any(IRegistration.class), any(Envelope.class));
        queue.handleDelivery(consumerTag, envelope, properties, body);
        verify(channel).basicAck(any(Long.class), eq(true));
    }

    @Test
    public void exceptionResultCallsAckFailure() throws IOException {
        final Object result = DeliveryOutcome.Exception;
        doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return result;
            }
        }).when(dispatcher).dispatch(any(IRegistration.class), any(Envelope.class));
        queue.handleDelivery(consumerTag, envelope, properties, body);
        verify(channel).basicNack(any(Long.class), eq(false), eq(false));
    }

    @Test
    public void actualExceptionResultCallsAckFailure() throws IOException {
        doThrow(RuntimeException.class).when(dispatcher).dispatch(any(IRegistration.class), any(Envelope.class));
        doThrow(RuntimeException.class).when(dispatcher).dispatchFailed(any(IRegistration.class), any(Envelope.class),
                        any(Exception.class));
        queue.handleDelivery(consumerTag, envelope, properties, body);
        verify(channel).basicNack(any(Long.class), eq(false), eq(false));
    }
}

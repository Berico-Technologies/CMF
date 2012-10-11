package cmf.bus.berico.rabbit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

@SuppressWarnings("serial")
public class TransportProviderTest {

    @Mock
    private ConnectionProvider connectionProvider;
    @Mock
    private QueueProvider queueProvider;
    @Mock
    private Queue queue;
    @Mock
    private ITopologyProvider topologyProvider;
    @Mock
    private IRegistration registration;
    @Mock
    private Channel channel;
    @Mock
    private Connection connection;
    @Mock
    private Route route;
    @Mock
    private Envelope envelope;
    private byte[] payload = new byte[] { 0, 0, 1, 1 };

    private TransportProvider transportProvider;

    @Before
    public void before() throws IOException {
        MockitoAnnotations.initMocks(this);

        doAnswer(new Answer<Connection>() {

            @Override
            public Connection answer(InvocationOnMock invocation) throws Throwable {
                return connection;
            }
        }).when(connectionProvider).newConnection();
        doAnswer(new Answer<Channel>() {

            @Override
            public Channel answer(InvocationOnMock invocation) throws Throwable {
                return channel;
            }
        }).when(connectionProvider).newChannel(any(Connection.class));
        doAnswer(new Answer<Queue>() {

            @Override
            public Queue answer(InvocationOnMock invocation) throws Throwable {
                return queue;
            }
        }).when(queueProvider).newQueue(any(Channel.class), any(IRegistration.class));
        transportProvider = new TransportProvider(connectionProvider, queueProvider, topologyProvider);
    }

    @Test
    public void constructorOpensConnection() throws IOException {
        verify(connectionProvider).newConnection();
    }

    @Test
    public void constructorCreatesChannel() throws IOException {
        verify(connectionProvider).newChannel(any(Connection.class));
    }

    @Test
    public void registerCreatesChannel() throws IOException {
        transportProvider.register(registration);
        verify(connectionProvider, times(2)).newChannel(any(Connection.class));
    }

    @Test
    public void registerCreatesQueue() {
        transportProvider.register(registration);
        verify(queueProvider).newQueue(any(Channel.class), any(IRegistration.class));
    }

    @Test
    public void registerRetreivesReceiveRoutes() {
        transportProvider.register(registration);
        verify(topologyProvider).getReceiveRoutes(anyString());
    }

    @Test
    public void registerBindsForEachRoute() throws IOException {
        initReceiveRoutes();
        transportProvider.register(registration);
        verify(queue, times(2)).bind(anyString(), anyString());
    }

    @Test
    public void sendPublishesForEachRoute() throws IOException {
        initSendRoutes();
        transportProvider.send(envelope);
        verify(channel, times(2)).basicPublish(anyString(), anyString(), any(BasicProperties.class), any(byte[].class));
    }

    @Test
    public void exchangeOnlyDeclaredOnce() throws IOException {
        initSendRoutes();
        final String exchangeName = "exchangeName1";
        doAnswer(new Answer<String>() {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return exchangeName;
            }
        }).when(route).getExchangeName();
        transportProvider.register(registration);
        transportProvider.send(envelope);
        verify(channel, times(1)).exchangeDeclare(anyString(), anyString(), anyBoolean());
    }

    @Test
    public void sendWritesPayloadToRabbitBody() throws IOException {
        initSendRoutes();
        doAnswer(new Answer<byte[]>() {

            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                return payload;
            }
        }).when(envelope).getPayload();
        transportProvider.send(envelope);
        ArgumentCaptor<byte[]> payloadCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(channel, times(2)).basicPublish(anyString(), anyString(), any(BasicProperties.class),
                        payloadCaptor.capture());
        assertEquals(payload, payloadCaptor.getValue());
    }

    @Test
    public void sendWritesEnvelopeHeadersToRabbitProperties() throws IOException {
        initSendRoutes();
        final String key1 = "key1";
        final String value1 = "value1";
        final String key2 = "key2";
        final String value2 = "value2";
        doAnswer(new Answer<Map<String, String>>() {

            @Override
            public HashMap<String, String> answer(InvocationOnMock invocation) throws Throwable {
                return new HashMap<String, String>() {

                    {
                        put(key1, value1);
                        put(key2, value2);
                    }
                };
            }
        }).when(envelope).getHeaders();
        transportProvider.send(envelope);
        ArgumentCaptor<BasicProperties> basicPropertiesCaptor = ArgumentCaptor.forClass(BasicProperties.class);
        verify(channel, times(2)).basicPublish(anyString(), anyString(), basicPropertiesCaptor.capture(),
                        any(byte[].class));
        BasicProperties basicProperties = basicPropertiesCaptor.getValue();
        Map<String, Object> capturedBasicProperties = basicProperties.getHeaders();
        assertEquals(value1, capturedBasicProperties.get(key1));
        assertEquals(value2, capturedBasicProperties.get(key2));
    }

    private void initSendRoutes() {
        doAnswer(new Answer<Collection<Route>>() {

            @Override
            public Collection<Route> answer(InvocationOnMock invocation) throws Throwable {
                return new ArrayList<Route>() {

                    {
                        add(route);
                        add(route);
                    }
                };
            }
        }).when(topologyProvider).getSendRoutes(anyString());
    }

    private void initReceiveRoutes() {
        doAnswer(new Answer<Collection<Route>>() {

            @Override
            public Collection<Route> answer(InvocationOnMock invocation) throws Throwable {
                return new ArrayList<Route>() {

                    {
                        add(route);
                        add(route);
                    }
                };
            }
        }).when(topologyProvider).getReceiveRoutes(anyString());
    }
}

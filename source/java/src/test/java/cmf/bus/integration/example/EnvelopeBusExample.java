package cmf.bus.integration.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.bus.IRegistration;
import cmf.bus.berico.BlockingEnvelopeHandler;
import cmf.bus.berico.DefaultEnvelopeBus;
import cmf.bus.berico.DefaultEnvelopeDispatcher;
import cmf.bus.berico.DefaultEnvelopeRegistration;
import cmf.bus.berico.ITransportProvider;
import cmf.bus.berico.rabbit.ConnectionFactory;
import cmf.bus.berico.rabbit.ConnectionProvider;
import cmf.bus.berico.rabbit.ITopologyProvider;
import cmf.bus.berico.rabbit.QueueProvider;
import cmf.bus.berico.rabbit.RabbitTransportProvider;
import cmf.bus.berico.rabbit.support.InMemoryTopologyProvider;
import cmf.bus.berico.rabbit.support.InMemoryTopologyRegistry;
import cmf.bus.berico.rabbit.support.RabbitEnvelopeHelper;
import cmf.bus.berico.rabbit.support.RabbitRegistrationHelper;
import cmf.bus.eventing.ISerializer;
import cmf.bus.eventing.berico.GsonSerializer;
import cmf.bus.integration.example.messages.EventTypeA;

public class EnvelopeBusExample {

    private IEnvelopeBus receiverClient;
    private IEnvelopeBus senderClient;
    private ISerializer serializer;
    private String username = "guest";
    private String password = "guest";
    private String virtualHost = "/";
    private String host = "localhost";
    private int port = 5672;
    private String senderClientProfile = "test-senderClient";
    private String receiverClientProfile = "test-receiverClient";
    private String receiveExchange = "test-default";
    private String sendExchange = "test-default";
    private int timeout = 5;
    private Object event;

    @Before
    public void before() {
        senderClient = newEnvelopeBus(senderClientProfile);
        receiverClient = newEnvelopeBus(receiverClientProfile);
        serializer = new GsonSerializer();
        event = new EventTypeA("simple send receive test message");
    }

    private Envelope generateEnvelope() {
        Envelope envelope = new Envelope();
        envelope.setPayload(serializer.byteSerialize(event));
        RabbitEnvelopeHelper.Headers.setType(envelope, event);

        return envelope;
    }

    private IRegistration generateRegistration() {
        IRegistration registration = new DefaultEnvelopeRegistration();
        registration.setHandler(new BlockingEnvelopeHandler(timeout));
        RabbitRegistrationHelper.RegistrationInfo.setRoutingKey(registration, event);

        return registration;
    }

    private IEnvelopeBus newEnvelopeBus(String profile) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        ConnectionProvider connectionProvider = new ConnectionProvider(connectionFactory);
        QueueProvider queueProvider = new QueueProvider();
        ITopologyProvider topologyProvider =
                        new InMemoryTopologyProvider(profile, new InMemoryTopologyRegistry(receiveExchange,
                                        sendExchange));
        ITransportProvider transportProvider =
                        new RabbitTransportProvider(connectionProvider, queueProvider, topologyProvider);
        IEnvelopeBus envelopeBus = new DefaultEnvelopeBus(transportProvider, new DefaultEnvelopeDispatcher());

        return envelopeBus;
    }

    @Test
    public void simpleSendReceiveTest() {
        IRegistration registration = generateRegistration();
        receiverClient.register(registration);

        Envelope sentEnvelope = generateEnvelope();
        senderClient.send(sentEnvelope);

        Envelope receivedEnvelope = ((BlockingEnvelopeHandler) registration.getHandler()).getEnvelope();

        assertTrue(sentEnvelope.equals(receivedEnvelope));
        assertEquals(sentEnvelope, receivedEnvelope);
    }
}

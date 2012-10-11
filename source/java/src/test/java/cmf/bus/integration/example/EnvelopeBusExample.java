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
import cmf.bus.berico.IEnvelopeDispatcher;
import cmf.bus.berico.ITransportProvider;
import cmf.bus.berico.rabbit.ConnectionFactory;
import cmf.bus.berico.rabbit.ConnectionProvider;
import cmf.bus.berico.rabbit.ITopologyProvider;
import cmf.bus.berico.rabbit.QueueProvider;
import cmf.bus.berico.rabbit.TransportProvider;
import cmf.bus.berico.rabbit.support.InMemoryTopologyProvider;
import cmf.bus.berico.rabbit.support.InMemoryTopologyRegistry;
import cmf.bus.eventing.ISerializer;
import cmf.bus.eventing.berico.GsonSerializer;
import cmf.bus.integration.example.messages.EventTypeA;

public class EnvelopeBusExample {

    private IEnvelopeBus receiverClient;
    private IEnvelopeBus senderClient;
    private ISerializer serializer;

    @Before
    public void before() {
        senderClient = newEnvelopeBus("test-senderClient");
        receiverClient = newEnvelopeBus("test-receiverClient");
        serializer = new GsonSerializer();
    }

    private Envelope generateEnvelope() {
        Envelope envelope = new Envelope();
        Object event = new EventTypeA("simple send receive test message");
        envelope.setPayload(serializer.byteSerialize(event));
        envelope.setHeader("key1", "value1");
        envelope.setHeader("key2", "value2");

        return envelope;
    }

    private IEnvelopeBus newEnvelopeBus(String profile) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        ConnectionProvider connectionProvider = new ConnectionProvider(connectionFactory);
        IEnvelopeDispatcher envelopeDispatcher = new DefaultEnvelopeDispatcher();
        QueueProvider queueProvider = new QueueProvider(envelopeDispatcher);
        ITopologyProvider topologyProvider =
                        new InMemoryTopologyProvider("envelope-example", new InMemoryTopologyRegistry("test-default",
                                        "test-default"));
        ITransportProvider transportProvider =
                        new TransportProvider(connectionProvider, queueProvider, topologyProvider);
        IEnvelopeBus envelopeBus = new DefaultEnvelopeBus(transportProvider);

        return envelopeBus;
    }

    private BlockingEnvelopeHandler register(IEnvelopeBus envelopeBus) {
        BlockingEnvelopeHandler blockingEnvelopeHandler = new BlockingEnvelopeHandler(50);
        IRegistration registration = new DefaultEnvelopeRegistration();
        registration.setHandler(blockingEnvelopeHandler);
        envelopeBus.register(registration);

        return blockingEnvelopeHandler;
    }

    @Test
    public void simpleSendReceiveTest() {
        BlockingEnvelopeHandler handler = register(receiverClient);
        Envelope sentEnvelope = generateEnvelope();
        senderClient.send(sentEnvelope);
        Envelope receivedEnvelope = handler.getEnvelope();
        assertTrue(sentEnvelope.equals(receivedEnvelope));
        assertEquals(sentEnvelope, receivedEnvelope);
    }
}

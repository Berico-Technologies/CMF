package cmf.bus.berico.rabbit;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;
import cmf.bus.berico.IEnvelopeDispatcher;
import cmf.bus.berico.ITransportProvider;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TransportProvider implements ITransportProvider {

    public static final int DEFAULT_QUEUE_LIFETIME = 1000 * 60 * 30; // 30 minute lifetime
    private static final String TOPIC_EXCHANGE_TYPE = "topic";

    private Channel channel;
    private Connection connection;
    private ConnectionFactory connectionFactory;
    private Set<String> exchangesKnownToExist = new HashSet<String>();
    private int queueLifetime = DEFAULT_QUEUE_LIFETIME;
    private IEnvelopeDispatcher envelopeDispatcher;
    private ITopologyProvider topologyProvider;

    public TransportProvider(ConnectionFactory connectionFactory, ITopologyProvider topologyProvider, IEnvelopeDispatcher envelopeDispatcher) {
        this.connectionFactory = connectionFactory;
        this.topologyProvider = topologyProvider;
        this.envelopeDispatcher = envelopeDispatcher;
        openConnectionAndChannel();
    }
    
    private void closeConnectionAndChannel() {
        try {
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing the rabbit connection and channels", e);
        }
    }

    private void createExchange(String exchangeName) {
        try {
            channel.exchangeDeclare(exchangeName, TOPIC_EXCHANGE_TYPE, true);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error declaring exchange \"%s\"", exchangeName), e);
        }
    }

    @Override
    protected void finalize() {
        closeConnectionAndChannel();
    }

    private String getExchangeName(Route route) {
        String exchangeName = route.getExchangeName();
        if (exchangesKnownToExist.add(exchangeName)) {
            createExchange(exchangeName);
            exchangesKnownToExist.add(exchangeName);
        }

        return exchangeName;
    }

    private void openConnectionAndChannel() {
        try {
            if (connection != null) {
                connection.close();
            }
            connection = connectionFactory.newConnection();
        } catch (IOException e) {
            throw new RuntimeException("Error opening the rabbit connection", e);
        }
        channel = createChannel();
    }
    
    private Channel createChannel() {
        try {
            return connection.createChannel();
        } catch (IOException e) {
            throw new RuntimeException("Error opening rabbit command channel", e);
        }
    }

    public void register(IRegistration registration) {
        String routingKey = registration.getRegistrationInfo().get(RegistrationConstants.ROUTING_KEY);
        Collection<Route> routes = topologyProvider.getReceiveRoutes(routingKey);
        Queue queue = new Queue(createChannel(), queueLifetime, registration, envelopeDispatcher);
        
        for (Route route : routes) {
            queue.bind(getExchangeName(route), routingKey);
        }
    }

    public void send(Envelope envelope) {
        String routingKey = envelope.getHeader(EnvelopeConstants.TYPE);
        List<Route> sendRoutes = topologyProvider.getSendRoutes(routingKey);
        for (Route route : sendRoutes) {
            String exchangeName = getExchangeName(route);
            try {
                BasicProperties basicProperties = new BasicProperties.Builder().build();
                channel.basicPublish(exchangeName, routingKey, basicProperties, envelope.getPayload());
            } catch (IOException e) {
                throw new RuntimeException("Error sending envelope", e);
            }
        }
    }

    public void setQueueLifetime(int queueLifetime) {
        this.queueLifetime = queueLifetime;
    }
}

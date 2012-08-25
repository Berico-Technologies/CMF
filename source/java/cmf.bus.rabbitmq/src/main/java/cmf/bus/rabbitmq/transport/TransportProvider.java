package cmf.bus.rabbitmq.transport;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.BasicProperties;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.IRegistration;
import cmf.bus.core.ITransportProvider;
import cmf.bus.pubsub.Envelope;
import cmf.bus.pubsub.Registration;
import cmf.bus.pubsub.transport.Route;
import cmf.bus.pubsub.transport.TopologyProvider;

public class TransportProvider implements ITransportProvider {

    private static final String TOPIC_EXCHANGE_TYPE = "topic";
    private static final int DEFAULT_QUEUE_LIFETIME = 1000 * 60 * 30; // 30 minute lifetime

    private Set<String> exchangesKnownToExist = new HashSet<String>();
    private TopologyProvider topologyProvider;
    private Channel channel;
    private Connection connection;
    private ConnectionFactory connectionFactory;
    private int queueLifetime = DEFAULT_QUEUE_LIFETIME;
    private TopologyUpdateHandler topologyUpdateHandler

    public TransportProvider(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        openConnectionAndChannel();
    }

    public void setTopologyProvider(TopologyProvider topologyProvider) {
        this.topologyProvider = topologyProvider;
    }

    private String getExchangeName(Route route) {
        String exchangeName = route.getExchangeName();
        if (exchangesKnownToExist.add(exchangeName)) {
            createExchange(exchangeName);
            exchangesKnownToExist.add(exchangeName);
            // /* TODO
            // * add a binding equal to the subscription queueName for each exchange to allow bi-directional comms
            // */
            // broker.bindQueue(queueName, exchangeName, queueName);
        }

        return exchangeName;
    }

    @Override
    public void register(IRegistration registration) {
        if (!(registration instanceof Registration)) {
            throw new IllegalArgumentException("Registration must be of type PubSubRegistration");
        }
    }

    private void register(Registration registration) {
        Collection<Route> routes = topologyProvider.getReceiveRoutes(registration);
        String queueName = registration.getQueueName();
        Map<String, Object> params = new HashMap<String, Object>();
        Queue queue = null;
        params.put("x-expires", queueLifetime);
        try {
            channel.queueDeclare(queueName, false, false, false, params);
            queue = new Queue(connection.createChannel(), queueName, registration.getEnvelopeHandler());
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error creating queue \"%s\"", queueName), e);
        }

        for (Route route : routes) {
            String exchangeName = getExchangeName(route);
            String routingKey = route.getRoutingKey();
            String binding = null;
            if (!StringUtils.isBlank(routingKey)) {
                binding = routingKey;
            } else {
                binding = registration.getTopic();
            }
            queue.bind(exchangeName, binding);
        }
    }

    @Override
    public void send(IEnvelope envelope) {
        if (!(envelope instanceof Envelope)) {
            throw new IllegalArgumentException("Envelope must be of type PubSubEnvelope");
        }
        if (envelope.getTimestamp() == null) {
            envelope.setTimestamp(Calendar.getInstance().getTime().toString());
        }
        Envelope pubSubEnvelope = (Envelope) envelope;
        Collection<Route> routes = topologyProvider.getSendRoutes(pubSubEnvelope);
        for (Route route : routes) {
            String exchangeName = getExchangeName(route);

            String replyingTo = pubSubEnvelope.getReplyingTo();
            String routingKey = route.getRoutingKey();
            String topic = null;
            if (!StringUtils.isBlank(replyingTo)) {
                topic = replyingTo;
            } else if (!StringUtils.isBlank(routingKey)) {
                topic = routingKey;
            } else {
                topic = pubSubEnvelope.getTopic();
            }
            try {
                BasicProperties basicProperties = new BasicProperties.Builder().build();
                channel.basicPublish(exchangeName, topic, basicProperties, envelope.getPayload());
            } catch (IOException e) {
                throw new RuntimeException("Error sending envelope", e);
            }
        }
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

    protected void finalize() {
        closeConnectionAndChannel();
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
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            throw new RuntimeException("Error opening rabbit command channel", e);
        }
    }

    public void setQueueLifetime(int queueLifetime) {
        this.queueLifetime = queueLifetime;
    }

    @Override
    protected void finalize() {
        closeConnectionAndChannel();
    }
    
}

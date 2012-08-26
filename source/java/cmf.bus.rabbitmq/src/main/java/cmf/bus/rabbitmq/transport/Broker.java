package cmf.bus.rabbitmq.transport;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import cmf.bus.pubsub.Envelope;
import cmf.bus.pubsub.Registration;
import cmf.bus.pubsub.transport.IBroker;
import cmf.bus.pubsub.transport.Route;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Broker implements IBroker {

    private static final String TOPIC_EXCHANGE_TYPE = "topic";

    private Channel channel;
    private Connection connection;
    private ConnectionFactory connectionFactory;
    private Set<String> exchangesKnownToExist = new HashSet<String>();
    private int queueLifetime = DEFAULT_QUEUE_LIFETIME;

    public Broker(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
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
            // /* TODO
            // * add a binding equal to the subscription queueName for each exchange to allow bi-directional comms
            // */
            // broker.bindQueue(queueName, exchangeName, queueName);
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
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            throw new RuntimeException("Error opening rabbit command channel", e);
        }
    }

    @Override
    public void register(Registration registration, Collection<Route> routes) {
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
    public void send(Envelope envelope, Collection<Route> routes) {
        if (envelope.getTimestamp() == null) {
            envelope.setTimestamp(Calendar.getInstance().getTime().toString());
        }
        for (Route route : routes) {
            String exchangeName = getExchangeName(route);

            String replyingTo = envelope.getReplyingTo();
            String routingKey = route.getRoutingKey();
            String topic = null;
            if (!StringUtils.isBlank(replyingTo)) {
                topic = replyingTo;
            } else if (!StringUtils.isBlank(routingKey)) {
                topic = routingKey;
            } else {
                topic = envelope.getTopic();
            }
            try {
                BasicProperties basicProperties = new BasicProperties.Builder().build();
                channel.basicPublish(exchangeName, topic, basicProperties, envelope.getPayload());
            } catch (IOException e) {
                throw new RuntimeException("Error sending envelope", e);
            }
        }
    }

    @Override
    public void setQueueLifetime(int queueLifetime) {
        this.queueLifetime = queueLifetime;
    }

}

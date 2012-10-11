package cmf.bus.berico.rabbit;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;
import cmf.bus.berico.IEnvelopeReceivedCallback;
import cmf.bus.berico.ITransportProvider;
import cmf.bus.berico.rabbit.support.RabbitEnvelopeHelper;
import cmf.bus.berico.rabbit.support.RabbitRegistrationHelper;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class RabbitTransportProvider implements ITransportProvider {

    private static final String TOPIC_EXCHANGE_TYPE = "topic";

    private Channel channel;
    private Connection connection;
    private Set<String> exchangesKnownToExist = new HashSet<String>();
    private ITopologyProvider topologyProvider;
    private QueueProvider queueProvider;
    private ConnectionProvider connectionProvider;
    private Map<IRegistration, Queue> queues = new HashMap<IRegistration, Queue>();
    private Map<IRegistration, IEnvelopeReceivedCallback> callbacks = new HashMap<IRegistration, IEnvelopeReceivedCallback>();

    public RabbitTransportProvider(ConnectionProvider connectionProvider, QueueProvider queueProvider,
                    ITopologyProvider topologyProvider) {
        this.connectionProvider = connectionProvider;
        this.queueProvider = queueProvider;
        this.topologyProvider = topologyProvider;
        connection = connectionProvider.newConnection();
        channel = connectionProvider.newChannel(connection);
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
        connectionProvider.closeConnection(connection);
    }

    private String getExchangeName(Route route) {
        String exchangeName = route.getExchangeName();
        if (exchangesKnownToExist.add(exchangeName)) {
            createExchange(exchangeName);
            exchangesKnownToExist.add(exchangeName);
        }

        return exchangeName;
    }

    @Override
    public void register(IRegistration registration, IEnvelopeReceivedCallback callback) {
        Channel channel = connectionProvider.newChannel(connection);
        Queue queue = queueProvider.newQueue(channel, registration, callback);
        queues.put(registration, queue);
        callbacks.put(registration, callback);

        String routingKey = RabbitRegistrationHelper.RegistrationInfo.getRoutingKey(registration);
        Collection<Route> routes = topologyProvider.getReceiveRoutes(routingKey);
        for (Route route : routes) {
            queue.bind(getExchangeName(route), routingKey);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void send(Envelope envelope) {
        String routingKey = RabbitEnvelopeHelper.Headers.getType(envelope);
        List<Route> sendRoutes = topologyProvider.getSendRoutes(routingKey);
        byte[] payload = envelope.getPayload();
        Map<String, Object> headers = new HashMap<String, Object>();
        for (Entry<String, String> entry : envelope.getHeaders().entrySet()) {
            headers.put(entry.getKey(), entry.getValue());
        }
        BasicProperties basicProperties = new BasicProperties.Builder().build();
        basicProperties.setHeaders(headers);
        for (Route route : sendRoutes) {
            String exchangeName = getExchangeName(route);
            try {
                channel.basicPublish(exchangeName, routingKey, basicProperties, payload);
            } catch (IOException e) {
                throw new RuntimeException("Error sending envelope", e);
            }
        }
    }

    @Override
    public void unregister(IRegistration registration) {
        Queue queue = queues.remove(registration);
        if (queue != null) {
            queue.stop();
            callbacks.remove(registration);
        }
    }
}

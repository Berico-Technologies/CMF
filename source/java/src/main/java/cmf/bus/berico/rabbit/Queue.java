package cmf.bus.berico.rabbit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;
import cmf.bus.berico.IEnvelopeDispatcher;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

public class Queue extends DefaultConsumer {

    private String consumerTag;
    private IRegistration registration;
    private IEnvelopeDispatcher envelopeDispatcher;
    private String queueName;
    private long deliveryTag;

    public Queue(Channel channel, int queueLifetime, IRegistration registration,
                    IEnvelopeDispatcher envelopeDispatcher) {
        super(channel);
        String queueName = registration.getRegistrationInfo().get(RegistrationConstants.QUEUE_NAME);
        if (queueName == null) {
            queueName = UUID.randomUUID().toString();
        };
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("x-expires", queueLifetime);
            channel.queueDeclare(queueName, false, false, false, params);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error creating queue \"%s\"", queueName));
        }
        this.queueName = queueName;
        this.registration = registration;
        this.envelopeDispatcher = envelopeDispatcher;
    }

    private void ackFailure(long deliveryTag) {
        try {
            getChannel().basicNack(deliveryTag, false, false);
        } catch (IOException e) {
            throw new RuntimeException("Error fail acking envelope", e);
        }
    }

    private void ackRetry(long deliveryTag) {
        try {
            getChannel().basicAck(deliveryTag, true);
        } catch (IOException e) {
            throw new RuntimeException("Error retry acking envelope", e);
        }
    }

    private void ackSuccess(long deliveryTag) {
        try {
            getChannel().basicAck(deliveryTag, false);
        } catch (IOException e) {
            throw new RuntimeException("Error accept acking envelope", e);
        }
    }

    public void bind(String exchangeName, String routingKey) {
        try {
            getChannel().queueBind(queueName, exchangeName, routingKey);
            getChannel().basicConsume(queueName, false, consumerTag, this);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Error binding queue \"%s\"", queueName), e);
        }
    }

    @Override
    protected void finalize() {
        try {
            getChannel().basicCancel(consumerTag);
            getChannel().close();
        } catch (IOException e) {
            throw new RuntimeException("Error trying to stop consuming", e);
        }
    }

    @Override
    public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope rabbitEnvelope,
                    BasicProperties properties, byte[] body) {
        DeliveryOutcome deliveryOutcome = DeliveryOutcome.Acknowledge;
        try {
            super.handleDelivery(consumerTag, rabbitEnvelope, properties, body);
            deliveryTag = rabbitEnvelope.getDeliveryTag();

            Envelope envelope = new Envelope();
            envelope.setPayload(body);
            for (Entry<String, Object> entry : properties.getHeaders().entrySet()) {
                envelope.setHeader(entry.getKey(), entry.getValue().toString());
            }

            Object result = null;
            try {
                result = envelopeDispatcher.dispatch(registration, envelope);
            } catch (Exception e) {
                result = envelopeDispatcher.dispatchFailed(registration, envelope, e);
            }

            if (result instanceof DeliveryOutcome) {
                deliveryOutcome = (DeliveryOutcome) result;
            }
        } catch (Exception e) {
            deliveryOutcome = DeliveryOutcome.Exception;
        }

        switch (deliveryOutcome) {
            case Acknowledge:
                ackSuccess(deliveryTag);
                break;

            case Null:
                ackSuccess(deliveryTag);
                break;

            case Reject:
                ackRetry(deliveryTag);
                break;

            case Exception:
                ackFailure(deliveryTag);
                break;

            default:
                throw new RuntimeException("Unknown delivery outcome type");
        }
    }
}

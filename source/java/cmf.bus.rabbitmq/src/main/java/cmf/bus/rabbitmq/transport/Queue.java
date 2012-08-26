package cmf.bus.rabbitmq.transport;

import java.io.IOException;

import cmf.bus.core.DeliveryOutcome;
import cmf.bus.core.IEnvelopeHandler;
import cmf.bus.core.serializer.ISerializer;
import cmf.bus.pubsub.Envelope;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

public class Queue extends DefaultConsumer {

    private String consumerTag;
    private IEnvelopeHandler envelopeHandler;
    private String queueName;
    private ISerializer serializer;

    public Queue(Channel channel, String queueName, IEnvelopeHandler envelopeHandler) {
        super(channel);
        this.queueName = queueName;
        this.envelopeHandler = envelopeHandler;
    }

    private void ackFailure(long deliveryTag) {
        try {
            getChannel().basicAck(deliveryTag, false);
        } catch (IOException e) {
            throw new RuntimeException("Error reject acking envelope", e);
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
        long deliveryTag = 0;
        DeliveryOutcome deliveryOutcome = DeliveryOutcome.Acknowledge;
        try {
            super.handleDelivery(consumerTag, rabbitEnvelope, properties, body);
            deliveryTag = rabbitEnvelope.getDeliveryTag();
            Envelope envelope = serializer.byteDeserialize(body, Envelope.class);
            deliveryOutcome = envelopeHandler.receive(envelope);
        } catch (Exception e) {
            deliveryOutcome = DeliveryOutcome.Exception;
        }

        switch (deliveryOutcome) {
            case Acknowledge:
                ackSuccess(deliveryTag);
                break;

            case Exception:
                ackFailure(deliveryTag);
                break;

            case Reject:
                ackFailure(deliveryTag);
                break;

            case Retry:
                ackRetry(deliveryTag);
                break;

            default:
                throw new RuntimeException("Unrecognized delivery outcome");
        }
    }

    public void setSerializer(ISerializer serializer) {
        this.serializer = serializer;
    }

}

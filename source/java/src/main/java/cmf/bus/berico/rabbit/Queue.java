package cmf.bus.berico.rabbit;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import cmf.bus.Envelope;
import cmf.bus.berico.IEnvelopeReceivedCallback;

import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;

public class Queue extends DefaultConsumer {

    private String queueName;
    private String consumerTag;
    private long deliveryTag;
    private IEnvelopeReceivedCallback callback;

    public Queue(Channel channel, IEnvelopeReceivedCallback callback, String queueName, String consumerTag) {
        super(channel);
        this.callback = callback;
        this.queueName = queueName;
        this.consumerTag = consumerTag;
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
        stop();
    }

    public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope rabbitEnvelope,
                    BasicProperties properties, byte[] body) {
        DeliveryOutcome deliveryOutcome = DeliveryOutcome.Acknowledge;
        try {
            deliveryTag = rabbitEnvelope.getDeliveryTag();

            Envelope envelope = new Envelope();
            envelope.setPayload(body);
            for (Entry<String, Object> entry : properties.getHeaders().entrySet()) {
                envelope.setHeader(entry.getKey(), entry.getValue().toString());
            }

            Object result = callback.handleReceive(envelope);
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

    private void ackFailure(long deliveryTag) {
        try {
            getChannel().basicNack(deliveryTag, false, false);
        } catch (IOException e) {
            throw new RuntimeException("Error on ackFailure", e);
        }
    }

    private void ackRetry(long deliveryTag) {
        try {
            getChannel().basicAck(deliveryTag, true);
        } catch (IOException e) {
            throw new RuntimeException("Error on ackRetry", e);
        }
    }

    private void ackSuccess(long deliveryTag) {
        try {
            getChannel().basicAck(deliveryTag, false);
        } catch (IOException e) {
            throw new RuntimeException("Error on ackSuccess", e);
        }
    }

    public void stop() {
        try {
            getChannel().basicCancel(consumerTag);
            getChannel().close();
        } catch (IOException e) {
            throw new RuntimeException("Error stoping queue", e);
        }
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

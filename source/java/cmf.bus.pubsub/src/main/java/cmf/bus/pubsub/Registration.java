package cmf.bus.pubsub;

public class Registration extends cmf.bus.core.internal.Registration {

    protected String queueName;
    protected String topic;

    public String getQueueName() {
        return queueName;
    }

    public String getTopic() {
        return topic;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}

package cmf.bus.pubsub;

import cmf.bus.core.IEnvelopeHandler;
import cmf.bus.core.IRegistration;
import cmf.bus.core.ITransportFilter;

public class Registration implements IRegistration {

    protected IEnvelopeHandler envelopeHandler;
    protected String queueName;

    protected String topic;

    protected ITransportFilter transportFilter;

    @Override
    public IEnvelopeHandler getEnvelopeHandler() {
        return envelopeHandler;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public ITransportFilter getTransportFilter() {
        return transportFilter;
    }

    @Override
    public void setEnvelopeHandler(IEnvelopeHandler envelopeHandler) {
        this.envelopeHandler = envelopeHandler;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public void setTransportFilter(ITransportFilter transportFilter) {
        this.transportFilter = transportFilter;
    }

}

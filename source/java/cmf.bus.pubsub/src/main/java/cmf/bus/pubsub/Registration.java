package cmf.bus.pubsub;

import cmf.bus.core.IEnvelopeHandler;
import cmf.bus.core.IRegistration;
import cmf.bus.core.ITransportFilter;

import org.apache.commons.lang.StringUtils;

public class Registration implements IRegistration {

    protected IEnvelopeHandler envelopeHandler;
    protected String queueName;
    protected String topic;
    protected ITransportFilter transportFilter;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Registration)) {
            return false;
        }
        Registration other = (Registration) obj;
        if (envelopeHandler == null) {
            if (other.envelopeHandler != null) {
                return false;
            }
        } else if (!envelopeHandler.equals(other.envelopeHandler)) {
            return false;
        }
        if (queueName == null) {
            if (other.queueName != null) {
                return false;
            }
        } else if (!queueName.equals(other.queueName)) {
            return false;
        }
        if (topic == null) {
            if (other.topic != null) {
                return false;
            }
        } else if (!topic.equals(other.topic)) {
            return false;
        }
        if (transportFilter == null) {
            if (other.transportFilter != null) {
                return false;
            }
        } else if (!transportFilter.equals(other.transportFilter)) {
            return false;
        }

        return true;
    }

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
        if (envelopeHandler == null) {
            throw new IllegalArgumentException("Registration envelopeHandler cannot be set to null");
        }
        this.envelopeHandler = envelopeHandler;
    }

    public void setQueueName(String queueName) {
        if (StringUtils.isBlank(queueName)) {
            throw new IllegalArgumentException("Registration queueName cannot be set to null or an empty string");
        }
        this.queueName = queueName;
    }

    public void setTopic(String topic) {
        if (StringUtils.isBlank(topic)) {
            throw new IllegalArgumentException("Registration topic cannot be set to null or an empty string");
        }
        this.topic = topic;
    }

    @Override
    public void setTransportFilter(ITransportFilter transportFilter) {
        if (transportFilter == null) {
            throw new IllegalArgumentException("Registration transportFilter cannot be set to null");
        }
        this.transportFilter = transportFilter;
    }

}

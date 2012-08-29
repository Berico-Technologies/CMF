package cmf.bus.core;


import org.apache.commons.lang.StringUtils;

public class Registration implements IRegistration {

    protected IEnvelopeHandler envelopeHandler;
    protected String topic;

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
        if (topic == null) {
            if (other.topic != null) {
                return false;
            }
        } else if (!topic.equals(other.topic)) {
            return false;
        }

        return true;
    }

    @Override
    public IEnvelopeHandler getEnvelopeHandler() {
        return envelopeHandler;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public void setEnvelopeHandler(IEnvelopeHandler envelopeHandler) {
        if (envelopeHandler == null) {
            throw new IllegalArgumentException("Registration envelopeHandler cannot be set to null");
        }
        this.envelopeHandler = envelopeHandler;
    }

    public void setTopic(String topic) {
        if (StringUtils.isBlank(topic)) {
            throw new IllegalArgumentException("Registration topic cannot be set to null or an empty string");
        }
        this.topic = topic;
    }

}

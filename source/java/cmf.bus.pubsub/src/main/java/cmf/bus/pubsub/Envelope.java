package cmf.bus.pubsub;

import org.apache.commons.lang.StringUtils;

public class Envelope extends cmf.bus.core.internal.Envelope {

    public static final String TOPIC = "bus.envelope.topic";

    public String getTopic() {
        return getHeader(TOPIC);
    }

    public void setTopic(String topic) {
        if (StringUtils.isBlank(topic)) {
            throw new IllegalArgumentException("Envelope topic cannot be set to null or an empty string");
        }
        setHeader(TOPIC, topic);
    }

}

package cmf.eventing;

import java.util.Map;

import cmf.bus.Envelope;

public interface IOutboundEventProcessor {

    void processOutbound(Object event, Envelope envelope, Map<String, Object> context);
}

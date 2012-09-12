package cmf.bus.eventing;

import java.util.Map;

import cmf.bus.Envelope;

public interface IOutboundEventProcessor {

    Object processOutbound(Object event, Envelope envelope, Map<String, Object> context);
}

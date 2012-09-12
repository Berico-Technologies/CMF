package cmf.bus.eventing;

import java.util.Map;

import cmf.bus.Envelope;

public interface IInboundEventProcessor {

    Object processInbound(Object event, Envelope envelope, Map<String, Object> context);
}

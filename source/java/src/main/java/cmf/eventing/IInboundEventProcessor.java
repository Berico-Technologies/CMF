package cmf.eventing;

import java.util.Map;

import cmf.bus.Envelope;

public interface IInboundEventProcessor {

    boolean processInbound(Object event, Envelope envelope, Map<String, Object> context) throws Exception;
}

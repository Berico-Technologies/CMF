package cmf.bus.core.processor;

import java.util.Map;

import cmf.bus.core.IEnvelope;

public interface IInboundProcessor {

    void processInbound(IEnvelope envelope, Map<String, Object> context);

}

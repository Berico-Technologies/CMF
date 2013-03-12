package cmf.eventing.default;

import cmf.bus.Envelope;

public interface IInboundProcessorCallback {
	Object ProcessInbound(Envelope envelope) throws Exception;
}

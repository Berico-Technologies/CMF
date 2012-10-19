package cmf.eventing.berico;

public interface IOutboundEventProcessor {

    void processOutbound(ProcessingContext context) throws Exception;
}

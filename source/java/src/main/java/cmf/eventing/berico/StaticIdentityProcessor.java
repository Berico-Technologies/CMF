package cmf.eventing.berico;

import cmf.bus.berico.EnvelopeHelper;

public class StaticIdentityProcessor implements IOutboundEventProcessor {

    private String identity;

    public StaticIdentityProcessor(String identity) {
        this.identity = identity;
    }

    @Override
    public void processOutbound(ProcessingContext context) throws Exception {
        EnvelopeHelper env = new EnvelopeHelper(context.getEnvelope());
        env.setSenderIdentity(identity);
    }
}

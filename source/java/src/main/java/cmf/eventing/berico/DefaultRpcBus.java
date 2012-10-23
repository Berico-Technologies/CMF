package cmf.eventing.berico;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.Duration;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.bus.berico.EnvelopeHeaderConstants;
import cmf.bus.berico.EnvelopeHelper;
import cmf.eventing.patterns.rpc.IRpcEventBus;

public class DefaultRpcBus extends DefaultEventBus implements IRpcEventBus {

    public DefaultRpcBus(IEnvelopeBus envelopeBus) {
        super(envelopeBus);
    }

    public DefaultRpcBus(IEnvelopeBus envelopeBus, List<IInboundEventProcessor> inboundProcessors,
                    List<IOutboundEventProcessor> outboundProcessors) {
        super(envelopeBus, inboundProcessors, outboundProcessors);
    }

    public Object getResponseTo(Object request, Duration timeout, String expectedTopic) {
        log.debug("Enter GetResponseTo");

        if (null == request) {
            throw new IllegalArgumentException("Cannot get response to a null request");
        }

        // the response we're going to get
        Object response = null;

        try {
            Envelope env = new Envelope();
            EnvelopeHelper envelopeHelper = new EnvelopeHelper(env);

            // create the ID for the request and set it on the envelope
            UUID requestId = UUID.randomUUID();

            envelopeHelper.setMessageId(requestId);

            // add pattern & timeout information to the headers
            envelopeHelper.setMessagePattern(EnvelopeHeaderConstants.MESSAGE_PATTERN_RPC);
            envelopeHelper.setRpcTimeout(timeout);

            // let the outbound processor do its thing
            this.processOutbound(request, env);

            // create an RPC registration
            RpcRegistration rpcRegistration = new RpcRegistration(requestId, expectedTopic, inboundProcessors);

            // register with the envelope bus
            envelopeBus.register(rpcRegistration);

            // now that we're setup to receive the response, send the request
            envelopeBus.send(env);

            // get the envelope from the registraton
            response = rpcRegistration.getResponse(timeout);

            // unregister from the bus
            envelopeBus.unregister(rpcRegistration);
        } catch (Exception ex) {
            log.error("Exception publishing an event", ex);
            throw new RuntimeException("Exception publishing an event", ex);
        }

        log.debug("Leave GetResponseTo");
        return response;
    }

    @SuppressWarnings("unchecked")
    public <TResponse> TResponse getResponseTo(Object request, Duration timeout, Class<TResponse> expectedType) {
        Object responseObject = getResponseTo(request, timeout, expectedType.getCanonicalName());

        return (TResponse) responseObject;
    }

    @SuppressWarnings("rawtypes")
    public Collection gatherResponsesTo(Object request, Duration timeout, String... expectedTopics) {
        throw new UnsupportedOperationException();
    }

    public <TResponse> Collection<TResponse> gatherResponsesTo(Object request, Duration timeout) {
        throw new UnsupportedOperationException();
    }

    public void respondTo(Map<String, String> headers, Object response) {
        log.debug("Enter RespondTo");

        if (null == response) {
            throw new IllegalArgumentException("Cannot respond with a null event");
        }
        if (null == headers) {
            throw new IllegalArgumentException("Must provide non-null request headers");
        }

        Envelope envelope = new Envelope();
        envelope.setHeaders(headers);
        EnvelopeHelper originalHeadersHelper = new EnvelopeHelper(envelope);
        if (null == originalHeadersHelper.getMessageId()) {
            throw new IllegalArgumentException(
                            "Cannot respond to a request because the provided request headers do not contain a message ID");
        }

        try {
            Envelope env = new Envelope();
            new EnvelopeHelper(env).setCorrelationId(originalHeadersHelper.getMessageId());

            this.processOutbound(response, env);
            envelopeBus.send(env);
        } catch (Exception ex) {
            log.error("Exception responding to an event", ex);
            throw new RuntimeException("Exception responding to an event", ex);
        }

        log.debug("Leave RespondTo");
    }
}

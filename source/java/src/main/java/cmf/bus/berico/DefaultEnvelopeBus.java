package cmf.bus.berico;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.bus.IEnvelopeHandler;
import cmf.bus.IRegistration;

public class DefaultEnvelopeBus implements IEnvelopeBus {

    protected class EnvelopeBusEnvelopeHandler implements IEnvelopeHandler {

        private IEnvelopeHandler userEnvelopeHandler;

        public EnvelopeBusEnvelopeHandler(IEnvelopeHandler userEnvelopeHandler) {
            this.userEnvelopeHandler = userEnvelopeHandler;
        }

        @Override
        public Object handle(Envelope envelope) {
            processInbound(envelope);
            Object result = userEnvelopeHandler.handle(envelope);

            return result;
        }

        @Override
        public Object handleFailed(Envelope envelope, Exception e) {
            return userEnvelopeHandler.handleFailed(envelope, e);
        }
    }

    protected List<IInboundEnvelopeProcessor> inboundProcessors = new LinkedList<IInboundEnvelopeProcessor>();
    protected List<IOutboundEnvelopeProcessor> outboundProcessors = new LinkedList<IOutboundEnvelopeProcessor>();
    protected ITransportProvider transportProvider;

    public DefaultEnvelopeBus(ITransportProvider transportProvider) {
        this.transportProvider = transportProvider;
    }

    protected void processInbound(Envelope envelope) {
        Map<String, Object> context = new HashMap<String, Object>();
        for (IInboundEnvelopeProcessor inboundEnvelopeProcessor : inboundProcessors) {
            inboundEnvelopeProcessor.processInbound(envelope, context);
        }
    }

    protected void processOutbound(Envelope envelope) {
        Map<String, Object> context = new HashMap<String, Object>();
        for (IOutboundEnvelopeProcessor outboundEnvelopeProcessor : outboundProcessors) {
            outboundEnvelopeProcessor.processOutbound(envelope, context);
        }
    }

    @Override
    public void register(final IRegistration registration) {
        if (registration == null) {
            throw new IllegalArgumentException("Cannot register with a null registration");
        }
        /**
         * wrap user envelope handler with bus envelope handler which calls processInbound to run the inbound processors
         * before dispatching an envelope
         */
        registration.setEnvelopeHandler(new EnvelopeBusEnvelopeHandler(registration.getEnvelopeHandler()));
        transportProvider.register(registration);
    }

    @Override
    public void send(Envelope envelope) {
        if (envelope == null) {
            throw new IllegalArgumentException("Cannot send a null envelope");
        }
        processOutbound(envelope);
        transportProvider.send(envelope);
    }

    public void setInboundProcessors(List<IInboundEnvelopeProcessor> inboundProcessors) {
        this.inboundProcessors = inboundProcessors;
    }

    public void setOutboundProcessorCollection(List<IOutboundEnvelopeProcessor> outboundProcessors) {
        this.outboundProcessors = outboundProcessors;
    }

    public void setTransportProvider(ITransportProvider transportProvider) {
        this.transportProvider = transportProvider;
    }
}

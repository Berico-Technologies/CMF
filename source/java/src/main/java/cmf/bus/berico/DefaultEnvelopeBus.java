package cmf.bus.berico;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.bus.IRegistration;

public class DefaultEnvelopeBus implements IEnvelopeBus {

    private List<IInboundEnvelopeProcessor> inboundProcessors = new LinkedList<IInboundEnvelopeProcessor>();
    private List<IOutboundEnvelopeProcessor> outboundProcessors = new LinkedList<IOutboundEnvelopeProcessor>();
    private ITransportProvider transportProvider;
    private IEnvelopeDispatcher envelopeDispatcher;

    public DefaultEnvelopeBus(ITransportProvider transportProvider, IEnvelopeDispatcher envelopeDispatcher) {
        this.transportProvider = transportProvider;
        this.envelopeDispatcher = envelopeDispatcher;
    }

    public DefaultEnvelopeBus(ITransportProvider transportProvider, List<IInboundEnvelopeProcessor> inboundProcessors,
                    List<IOutboundEnvelopeProcessor> outboundProcessors, IEnvelopeDispatcher envelopeDispatcher) {
        this.transportProvider = transportProvider;
        this.inboundProcessors = inboundProcessors;
        this.outboundProcessors = outboundProcessors;
        this.envelopeDispatcher = envelopeDispatcher;
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

        transportProvider.register(registration, new IEnvelopeReceivedCallback() {

            public Object handleReceive(Envelope envelope) {
                processInbound(envelope);

                return envelopeDispatcher.dispatch(registration, envelope);
            }
        });
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

    @Override
    public void unregister(IRegistration registration) {
        if (registration == null) {
            throw new IllegalArgumentException("Cannot unregister with a null registration");
        }
        transportProvider.unregister(registration);
    }
}

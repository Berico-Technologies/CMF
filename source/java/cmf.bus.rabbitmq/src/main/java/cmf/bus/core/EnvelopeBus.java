package cmf.bus.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cmf.bus.core.processor.IInboundEnvelopeProcessor;
import cmf.bus.core.processor.IOutboundEnvelopeProcessor;
import cmf.bus.core.transport.ITransportProvider;

public class EnvelopeBus implements IEnvelopeBus {

    protected Collection<IInboundEnvelopeProcessor> inboundProcessorCollection;
    protected Collection<IOutboundEnvelopeProcessor> outboundProcessorCollection;
    protected ITransportProvider transportProvider;

    @Override
    public void register(final IRegistration registration) {
        final IEnvelopeHandler userEnvelopeHandler = registration.getEnvelopeHandler();
        IEnvelopeHandler busEnvelopeHandler = new IEnvelopeHandler() {

            @Override
            public DeliveryOutcome handleEnvelope(Envelope envelope) {
                DeliveryOutcome deliveryOutcome = DeliveryOutcome.Acknowledge;
                try {
                    Map<String, Object> context = new HashMap<String, Object>();
                    for (IInboundEnvelopeProcessor inboundEnvelopeProcessor : inboundProcessorCollection) {
                        inboundEnvelopeProcessor.processInbound(envelope, context);
                    }
                    deliveryOutcome = userEnvelopeHandler.handleEnvelope(envelope);
                } catch (Exception e) {
                    deliveryOutcome = DeliveryOutcome.Exception;
                }

                return deliveryOutcome;
            }
        };
        registration.setEnvelopeHandler(busEnvelopeHandler);
        transportProvider.register(registration);
    }

    public Registration register(String topic, IEnvelopeHandler envelopeHandler) {
        Registration registration = new Registration();
        registration.setTopic(topic);
        registration.setEnvelopeHandler(envelopeHandler);
        register(registration);

        return registration;
    }
    
    @Override
    public void send(Envelope envelope) {
        Map<String, Object> context = new HashMap<String, Object>();
        for (IOutboundEnvelopeProcessor outboundEnvelopeProcessor : outboundProcessorCollection) {
            outboundEnvelopeProcessor.processOutbound(envelope, context);
        }
        transportProvider.send(envelope);
    }

    public void setInboundProcessorCollection(Collection<IInboundEnvelopeProcessor> inboundProcessorCollection) {
        this.inboundProcessorCollection = inboundProcessorCollection;
    }

    public void setOutboundProcessorCollection(Collection<IOutboundEnvelopeProcessor> outboundProcessorCollection) {
        this.outboundProcessorCollection = outboundProcessorCollection;
    }

    public void setTransportProvider(ITransportProvider transportProvider) {
        this.transportProvider = transportProvider;
    }

}

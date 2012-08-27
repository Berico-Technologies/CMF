package cmf.bus.pubsub;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cmf.bus.core.DeliveryOutcome;
import cmf.bus.core.IEnvelope;
import cmf.bus.core.IEnvelopeBus;
import cmf.bus.core.IEnvelopeHandler;
import cmf.bus.core.IRegistration;
import cmf.bus.core.ITransportFilter;
import cmf.bus.core.processor.IInboundProcessor;
import cmf.bus.core.processor.IOutboundProcessor;
import cmf.bus.core.transport.ITransportProvider;

public class EnvelopeBus implements IEnvelopeBus {

    protected Collection<IInboundProcessor> inboundProcessorCollection;
    protected Collection<IOutboundProcessor> outboundProcessorCollection;
    protected ITransportProvider transportProvider;

    @Override
    public void register(final IRegistration registration) {
        final IEnvelopeHandler userEnvelopeHandler = registration.getEnvelopeHandler();
        IEnvelopeHandler busEnvelopeHandler = new IEnvelopeHandler() {

            @Override
            public DeliveryOutcome receive(IEnvelope envelope) {
                DeliveryOutcome deliveryOutcome = DeliveryOutcome.Acknowledge;
                try {
                    Map<String, Object> context = new HashMap<String, Object>();
                    for (IInboundProcessor inboundProcessor : inboundProcessorCollection) {
                        inboundProcessor.processInbound(envelope, context);
                    }
                    // TODO - change this to use the dispatcher
                    deliveryOutcome = userEnvelopeHandler.receive(envelope);
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
    
    public Registration register(String queueName, String topic, IEnvelopeHandler envelopeHandler) {
        Registration registration = new Registration();
        registration.setQueueName(queueName);
        registration.setTopic(topic);
        registration.setEnvelopeHandler(envelopeHandler);
        register(registration);

        return registration;
    }
    
    public Registration register(String queueName, String topic, IEnvelopeHandler envelopeHandler, ITransportFilter transportFilter) {
        Registration registration = new Registration();
        registration.setQueueName(queueName);
        registration.setTopic(topic);
        registration.setEnvelopeHandler(envelopeHandler);
        registration.setTransportFilter(transportFilter);
        register(registration);

        return registration;
    }

    @Override
    public void send(IEnvelope envelope) {
        Map<String, Object> context = new HashMap<String, Object>();
        for (IOutboundProcessor outboundProcessor : outboundProcessorCollection) {
            outboundProcessor.processOutbound(envelope, context);
        }
        transportProvider.send(envelope);
    }

    public void setInboundProcessorCollection(Collection<IInboundProcessor> inboundProcessorCollection) {
        this.inboundProcessorCollection = inboundProcessorCollection;
    }

    public void setOutboundProcessorCollection(Collection<IOutboundProcessor> outboundProcessorCollection) {
        this.outboundProcessorCollection = outboundProcessorCollection;
    }

    public void setTransportProvider(ITransportProvider transportProvider) {
        this.transportProvider = transportProvider;
    }

}

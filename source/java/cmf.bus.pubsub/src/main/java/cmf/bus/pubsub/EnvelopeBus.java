package cmf.bus.pubsub;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cmf.bus.core.DeliveryOutcome;
import cmf.bus.core.IEnvelope;
import cmf.bus.core.IEnvelopeBus;
import cmf.bus.core.IEnvelopeHandler;
import cmf.bus.core.IRegistration;
import cmf.bus.core.ITransportProvider;
import cmf.bus.core.processor.IInboundProcessor;
import cmf.bus.core.processor.IOutboundProcessor;

public class EnvelopeBus implements IEnvelopeBus {

    protected Collection<IInboundProcessor> receiveModuleCollection;
    protected Collection<IOutboundProcessor> sendModuleCollection;
    protected ITransportProvider transportProvider;

    @Override
    public void register(final IRegistration registration) {
        registration.setEnvelopeHandler(new IEnvelopeHandler() {

            @Override
            public DeliveryOutcome receive(IEnvelope envelope) {
                DeliveryOutcome deliveryOutcome = DeliveryOutcome.Acknowledge;
                try {
                    Map<String, Object> context = new HashMap<String, Object>();
                    for (IInboundProcessor inboundProcessor : receiveModuleCollection) {
                        inboundProcessor.processInbound(envelope, context);
                    }
                    // TODO - change this to use the dispatcher
                    deliveryOutcome = registration.getEnvelopeHandler().receive(envelope);
                } catch (Exception e) {
                    deliveryOutcome = DeliveryOutcome.Exception;
                }

                return deliveryOutcome;
            }
        });
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
    public void send(IEnvelope envelope) {
        Map<String, Object> context = new HashMap<String, Object>();
        for (IOutboundProcessor outboundProcessor : sendModuleCollection) {
            outboundProcessor.processOutbound(envelope, context);
        }
        transportProvider.send(envelope);
    }

    public void setReceiveModuleCollection(Collection<IInboundProcessor> receiveModuleCollection) {
        this.receiveModuleCollection = receiveModuleCollection;
    }

    public void setSendModuleCollection(Collection<IOutboundProcessor> sendModuleCollection) {
        this.sendModuleCollection = sendModuleCollection;
    }

    public void setTransportProvider(ITransportProvider transportProvider) {
        this.transportProvider = transportProvider;
    }

}

package cmf.bus.eventing.berico;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.bus.IEnvelopeHandler;
import cmf.bus.IRegistration;
import cmf.bus.berico.DefaultEnvelopeRegistration;
import cmf.bus.eventing.IEventBus;
import cmf.bus.eventing.IEventFilterPredicate;
import cmf.bus.eventing.IEventHandler;
import cmf.bus.eventing.IInboundEventProcessor;
import cmf.bus.eventing.IOutboundEventProcessor;

@SuppressWarnings("unchecked")
public class DefaultEventBus implements IEventBus {

    protected class EventBusEnvelopeHandler<TEVENT> implements IEnvelopeHandler {

        private IEventHandler<TEVENT> userEventHandler;

        public EventBusEnvelopeHandler(IEventHandler<TEVENT> userEventHandler) {
            this.userEventHandler = userEventHandler;
        }

        @Override
        public Object handle(Envelope envelope) {
            TEVENT event = (TEVENT) processInbound(null, envelope);
            Object result = userEventHandler.handle(event, envelope.getHeaders());

            return result;
        }

        @Override
        public Object handleFailed(Envelope envelope, Exception e) {
            return userEventHandler.handleFailed(envelope, e);
        }
    }

    protected IEnvelopeBus envelopeBus;
    protected List<IInboundEventProcessor> inboundProcessors = new LinkedList<IInboundEventProcessor>();
    protected List<IOutboundEventProcessor> outboundProcessors = new LinkedList<IOutboundEventProcessor>();

    public DefaultEventBus(IEnvelopeBus envelopeBus, List<IInboundEventProcessor> inboundProcessors,
                    List<IOutboundEventProcessor> outboundProcessors) {
        this.envelopeBus = envelopeBus;
        this.inboundProcessors = inboundProcessors;
        this.outboundProcessors = outboundProcessors;
    }

    @Override
    public void publish(Object event) {
        if (event == null) {
            throw new IllegalArgumentException("Cannot publish a null event");
        }
        Envelope envelope = new Envelope();
        processOutbound(event, envelope);
        envelopeBus.send(envelope);
    }

    @Override
    public <TEVENT> void subscribe(IEventHandler<TEVENT> eventHandler) {
        Class<TEVENT> type = eventHandler.getEventType();
        IEventFilterPredicate filterPredicate = new TypeEventFilterPredicate(type);
        subscribe(eventHandler, filterPredicate);
    }

    @Override
    public <TEVENT> void subscribe(final IEventHandler<TEVENT> eventHandler, final IEventFilterPredicate filterPredicate) {
        IRegistration registration = new DefaultEnvelopeRegistration();
        registration.setEnvelopeHandler(new EventBusEnvelopeHandler<TEVENT>(eventHandler));
        envelopeBus.register(registration);
    }

    protected Object processOutbound(Object event, Envelope envelope) {
        Map<String, Object> context = new HashMap<String, Object>();
        for (IOutboundEventProcessor processor : outboundProcessors) {
            processor.processOutbound(event, envelope, context);
        }
        
        return event;
    }

    protected Object processInbound(Object event, Envelope envelope) {
        Map<String, Object> context = new HashMap<String, Object>();
        for (IInboundEventProcessor processor : inboundProcessors) {
            event = processor.processInbound(event, envelope, context);
        }
        
        return event;
    }
}

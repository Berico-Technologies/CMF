package cmf.eventing.berico;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.bus.IEnvelopeHandler;
import cmf.bus.IRegistration;
import cmf.bus.berico.DefaultEnvelopeRegistration;
import cmf.eventing.IEventBus;
import cmf.eventing.IEventFilterPredicate;
import cmf.eventing.IEventHandler;
import cmf.eventing.IInboundEventProcessor;
import cmf.eventing.IOutboundEventProcessor;

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

    protected Object processInbound(Object event, Envelope envelope) {
        Map<String, Object> context = new HashMap<String, Object>();
        for (IInboundEventProcessor processor : inboundProcessors) {
            event = processor.processInbound(event, envelope, context);
        }

        return event;
    }

    protected Object processOutbound(Object event, Envelope envelope) {
        Map<String, Object> context = new HashMap<String, Object>();
        for (IOutboundEventProcessor processor : outboundProcessors) {
            processor.processOutbound(event, envelope, context);
        }

        return event;
    }

    @Override
    public void publish(Object event) throws Exception {
        if (event == null) {
            throw new IllegalArgumentException("Cannot publish a null event");
        }
        Envelope envelope = new Envelope();
        processOutbound(event, envelope);
        envelopeBus.send(envelope);
    }

    @Override
    public <TEVENT> void subscribe(IEventHandler<TEVENT> eventHandler) throws Exception {
        Class<TEVENT> type = eventHandler.getEventType();
        IEventFilterPredicate filterPredicate = new TypeEventFilterPredicate(type);
        subscribe(eventHandler, filterPredicate);
    }

    @Override
    public <TEVENT> void subscribe(final IEventHandler<TEVENT> eventHandler, final IEventFilterPredicate filterPredicate) throws Exception {
        IRegistration registration = new DefaultEnvelopeRegistration();
        registration.setHandler(new EventBusEnvelopeHandler<TEVENT>(eventHandler));
        envelopeBus.register(registration);
    }
}

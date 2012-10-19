package cmf.eventing.berico;

import java.util.LinkedList;
import java.util.List;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeBus;
import cmf.eventing.IEventBus;
import cmf.eventing.IEventFilterPredicate;
import cmf.eventing.IEventHandler;

public class DefaultEventBus implements IEventBus {

    protected IEnvelopeBus envelopeBus;
    protected List<IInboundEventProcessor> inboundProcessors = new LinkedList<IInboundEventProcessor>();
    protected List<IOutboundEventProcessor> outboundProcessors = new LinkedList<IOutboundEventProcessor>();

    
    public DefaultEventBus(
    		IEnvelopeBus envelopeBus, 
    		List<IInboundEventProcessor> inboundProcessors,
            List<IOutboundEventProcessor> outboundProcessors) {
        this.envelopeBus = envelopeBus;
        this.inboundProcessors = inboundProcessors;
        this.outboundProcessors = outboundProcessors;
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
    	EventRegistration registration = new EventRegistration(eventHandler, this.inboundProcessors);
        envelopeBus.register(registration);
    }
    

    protected Object processOutbound(Object event, Envelope envelope) throws Exception {
        ProcessingContext context = new ProcessingContext(envelope, event);
        for (IOutboundEventProcessor processor : outboundProcessors) {
            processor.processOutbound(context);
        }

        return event;
    }
}

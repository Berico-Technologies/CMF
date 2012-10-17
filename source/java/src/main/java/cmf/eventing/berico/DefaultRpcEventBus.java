package cmf.eventing.berico;
//package cmf.bus.eventing.berico.rpc;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import cmf.bus.Envelope;
//import cmf.bus.IEnvelopeBus;
//import cmf.bus.IRegistration;
//import cmf.bus.berico.DefaultEnvelopeRegistration;
//import cmf.bus.berico.HeaderEnvelopeFilterPredicate;
//import cmf.bus.berico.rabbit.support.RabbitEnvelopeHelper;
//import cmf.bus.eventing.IInboundEventProcessor;
//import cmf.bus.eventing.IOutboundEventProcessor;
//import cmf.bus.eventing.berico.BlockingEventHandler;
//import cmf.bus.eventing.berico.DefaultEventBus;
//import cmf.bus.eventing.rpc.IRpcEventBus;
//
//public class DefaultRpcEventBus extends DefaultEventBus implements IRpcEventBus {
//
//    private static final long DEFAULT_TIMEOUT = 30000; // 30 seconds
//
//    public DefaultRpcEventBus(IEnvelopeBus envelopeBus, List<IInboundEventProcessor> inboundProcessors,
//                    List<IOutboundEventProcessor> outboundProcessors) {
//        super(envelopeBus, inboundProcessors, outboundProcessors);
//    }
//
//    @Override
//    public <T> T getResponseTo(Object request) {
//        return getResponseTo(request, DEFAULT_TIMEOUT);
//    }
//
//    @Override
//    public <T> T getResponseTo(Object request, long timeout) {
//        IRegistration registration = new DefaultEnvelopeRegistration();
//        BlockingEventHandler<T> blockingEventHandler = new BlockingEventHandler<T>(timeout);
//        registration.setHandler(new EventBusEnvelopeHandler<T>(blockingEventHandler));
//        Envelope envelope = new Envelope();
//        processOutbound(request, envelope);
//        Map<String, String> filterHeaders = new HashMap<String, String>();
//        String envelopeId = RabbitEnvelopeHelper.Headers.getId(envelope);
//        filterHeaders.put(RabbitEnvelopeHelper.Headers.getCorrelationId(envelope), envelopeId);
//        HeaderEnvelopeFilterPredicate headerEnvelopeFilterPredicate = new HeaderEnvelopeFilterPredicate(filterHeaders);
//        registration.setFilterPredicate(headerEnvelopeFilterPredicate);
//        envelopeBus.register(registration);
//        envelopeBus.send(envelope);
//
//        return blockingEventHandler.getEvent();
//    }
//
//    @Override
//    public void respondTo(Map<String, String> originalHeaders, Object response) {
//        Map<String, String> filterHeaders = new HashMap<String, String>();
//        String correlationId = RabbitEnvelopeHelper.Headers.getId(originalHeaders);
//        filterHeaders.put(RabbitEnvelopeHelper.Headers.getCorrelationId(originalHeaders), correlationId);
//        publish(response);
//    }
//}

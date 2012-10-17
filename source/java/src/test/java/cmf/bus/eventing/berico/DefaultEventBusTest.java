//package cmf.bus.eventing.berico;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.eq;
//import static org.mockito.Mockito.verify;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import cmf.bus.Envelope;
//import cmf.bus.IEnvelopeBus;
//import cmf.bus.IRegistration;
//import cmf.eventing.IEventHandler;
//import cmf.eventing.IInboundEventProcessor;
//import cmf.eventing.IOutboundEventProcessor;
//import cmf.eventing.berico.DefaultEventBus;
//
//@SuppressWarnings({ "unchecked", "serial" })
//public class DefaultEventBusTest {
//
//    @Mock
//    private Object event;
//    @Mock
//    private IEnvelopeBus envelopeBus;
//    @Mock
//    private IEventHandler<Object> eventHandler;
//    @Mock
//    private IInboundEventProcessor inboundProcessor;
//    @Mock
//    private IOutboundEventProcessor outboundProcessor;
//    private List<IInboundEventProcessor> inboundProcessors;
//    private List<IOutboundEventProcessor> outboundProcessors;
//
//    private DefaultEventBus eventBus;
//
//    @Before
//    public void before() {
//        MockitoAnnotations.initMocks(this);
//
//        inboundProcessors = new ArrayList<IInboundEventProcessor>() {
//
//            {
//                add(inboundProcessor);
//            }
//        };
//        outboundProcessors = new ArrayList<IOutboundEventProcessor>() {
//
//            {
//                add(outboundProcessor);
//            }
//        };
//        eventBus = new DefaultEventBus(envelopeBus, inboundProcessors, outboundProcessors);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void publishNullEventThrows() {
//        eventBus.publish((Object) null);
//    }
//
//    @Test
//    public void publishProcessesOutbound() {
//        eventBus.publish(event);
//        verify(outboundProcessor).processOutbound(eq(event), any(Envelope.class), any(Map.class));
//    }
//
//    @Test
//    public void publishSendsEnvelope() {
//        eventBus.publish(event);
//        verify(envelopeBus).send(any(Envelope.class));
//    }
//    
//    @Test(expected = Exception.class)
//    public void subscribeNullHandlerThrows() {
//        eventBus.subscribe((IEventHandler<Object>) null);
//    }
//    
//    @Test
//    public void subscribeCallsRegister() {
//        eventBus.subscribe(eventHandler);
//        verify(envelopeBus).register(any(IRegistration.class));
//    }
//}

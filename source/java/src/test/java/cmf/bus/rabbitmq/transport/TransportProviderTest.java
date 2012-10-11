// package cmf.bus.rabbitmq.transport;
//
// import static org.mockito.Matchers.any;
// import static org.mockito.Mockito.verify;
//
// import java.util.Collection;
//
// import org.junit.Before;
// import org.junit.Test;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
//
// import cmf.bus.Envelope;
// import cmf.bus.berico.Registration;
// import cmf.bus.berico.rabbit.TopologyProvider;
// import cmf.bus.berico.rabbit.TransportProvider;
//
// @SuppressWarnings("unchecked")
// public class TransportProviderTest {
//
// @Mock
// private TransportProvider transportProvider;
// @Mock
// private InMemoryTopologyProvider topologyProvider;
// @Mock
// private Registration registration;
// @Mock
// private Envelope envelope;
// private TransportProvider transportProvider;
//
// @Before
// public void before() {
// MockitoAnnotations.initMocks(this);
//
// transportProvider = new TransportProvider();
// transportProvider.setBroker(transportProvider);
// transportProvider.setTopologyProvider(topologyProvider);
// }
//
// @Test
// public void registerCallsTopologyProviderGetReceiveRouteCollection() {
// transportProvider.register(registration);
// verify(topologyProvider).getReceiveRouteCollection(any(Registration.class));
// }
//
// @Test
// public void registerCallsBrokerRegister() {
// transportProvider.register(registration);
// verify(transportProvider).register(any(Registration.class), any(Collection.class));
// }
//
// @Test
// public void sendCallsTopologyProviderGetSendRouteCollection() {
// transportProvider.send(envelope);
// verify(topologyProvider).getSendRouteCollection(any(Envelope.class));
// }
//
// @Test
// public void sendCallsBrokerSend() {
// transportProvider.send(envelope);
// verify(transportProvider).send(any(Envelope.class), any(Collection.class));
// }
//
// }

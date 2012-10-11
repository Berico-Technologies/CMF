// package cmf.bus.rabbitmq.transport;
//
// import static org.junit.Assert.assertEquals;
// import static org.mockito.Matchers.anyString;
// import static org.mockito.Mockito.doReturn;
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
// import cmf.bus.berico.rabbit.Route;
// import cmf.bus.berico.rabbit.TopologyProvider;
// import cmf.bus.berico.rabbit.TopologyRegistry;
//
// public class TopologyProviderTest {
//
// @Mock
// private TopologyRegistry topologyRegistry;
// @Mock
// private Collection<Route> receiveRouteCollection;
// @Mock
// private Collection<Route> sendRouteCollection;
// @Mock
// private Registration registration;
// @Mock
// private Envelope envelope;
// private String topic = "topic";
// private InMemoryTopologyProvider topologyProvider;
//
// @Before
// public void before() {
// MockitoAnnotations.initMocks(this);
//
// topologyProvider = new InMemoryTopologyProvider();
// topologyProvider.setTopologyRegistry(topologyRegistry);
// }
//
// @Test
// public void getReceiveRouteCollectionCallsTopologyRegistryGetReceiveRouteCollection() {
// doReturn(topic).when(registration).getTopic();
// doReturn(receiveRouteCollection).when(topologyRegistry).getReceiveRouteCollection(anyString());
// Collection<Route> result = topologyProvider.getReceiveRouteCollection(registration);
// verify(topologyRegistry).getReceiveRouteCollection(anyString());
// assertEquals(receiveRouteCollection, result);
// }
//
// @Test
// public void getSendRouteCollectionCallsTopologyRegistryGetSendRouteCollection() {
// doReturn(topic).when(envelope).getTopic();
// doReturn(sendRouteCollection).when(topologyRegistry).getSendRouteCollection(anyString());
// Collection<Route> result = topologyProvider.getSendRouteCollection(envelope);
// verify(topologyRegistry).getSendRouteCollection(anyString());
// assertEquals(sendRouteCollection, result);
// }
//
// }

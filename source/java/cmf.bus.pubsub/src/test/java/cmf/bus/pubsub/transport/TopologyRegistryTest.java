package cmf.bus.pubsub.transport;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TopologyRegistryTest {

    private TopologyRegistry topologyRegistry;
    @Mock
    private Map<String, Collection<Route>> receiveRouteMap;
    @Mock
    private Map<String, Collection<Route>> sendRouteMap;
    @Mock
    private Collection<Route> receiveRouteCollection;
    @Mock
    private Collection<Route> sendRouteCollection;
    private String topic = "topic";

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        topologyRegistry = new TopologyRegistry();
        topologyRegistry.setReceiveRouteMap(receiveRouteMap);
        topologyRegistry.setSendRouteMap(sendRouteMap);
    }

    @Test
    public void getReceiveRouteCollectionCallsMapGet() {
        doReturn(receiveRouteCollection).when(receiveRouteMap).get(anyString());
        Collection<Route> result = topologyRegistry.getReceiveRouteCollection(topic);
        verify(receiveRouteMap).get(anyString());
        assertEquals(receiveRouteCollection, result);
    }

    @Test
    public void getSendRouteCollectionCallsMapGet() {
        doReturn(sendRouteCollection).when(sendRouteMap).get(anyString());
        Collection<Route> result = topologyRegistry.getSendRouteCollection(topic);
        verify(sendRouteMap).get(anyString());
        assertEquals(sendRouteCollection, result);
    }

}

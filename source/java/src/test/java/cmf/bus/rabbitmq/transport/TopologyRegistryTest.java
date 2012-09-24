package cmf.bus.rabbitmq.transport;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cmf.bus.berico.rabbit.Route;
import cmf.bus.berico.rabbit.TopologyRegistry;

public class TopologyRegistryTest {

    private TopologyRegistry topologyRegistry;
    @Mock
    private Map<String, List<Route>> receiveRouteMap;
    @Mock
    private Map<String, List<Route>> sendRouteMap;
    @Mock
    private List<Route> receiveRoutes;
    @Mock
    private List<Route> sendRoutes;
    private String topic = "topic";

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        topologyRegistry = new TopologyRegistry();
        topologyRegistry.setReceiveRouteMap(receiveRouteMap);
        topologyRegistry.setSendRouteMap(sendRouteMap);
    }

    @Test
    public void getReceiveRoutesCallsMapGet() {
        doReturn(receiveRoutes).when(receiveRouteMap).get(anyString());
        List<Route> result = topologyRegistry.getReceiveRoutes(topic);
        verify(receiveRouteMap).get(anyString());
        assertEquals(receiveRoutes, result);
    }

    @Test
    public void getSendRoutesCallsMapGet() {
        doReturn(sendRoutes).when(sendRouteMap).get(anyString());
        List<Route> result = topologyRegistry.getSendRoutes(topic);
        verify(sendRouteMap).get(anyString());
        assertEquals(sendRoutes, result);
    }

}
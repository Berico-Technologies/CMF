package cmf.bus.berico.rabbit;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cmf.bus.Envelope;
import cmf.bus.eventing.IEventBus;
import cmf.bus.eventing.IEventHandler;

@SuppressWarnings({ "unchecked", "serial", "rawtypes" })
public class TopologyProviderTest {

    @Mock
    private IEventBus eventBus;
    private String profile = "topology-provider-test";
    @Mock
    private Route route;
    private String routingKey = "routingKey";
    private TopologyService topologyProvider;
    @Mock
    private ITopologyRegistry topologyRegistry;
    @Mock
    private ITopologyRegistry topologyRegistry2;
    @Mock
    private TopologyUpdateResponse topologyUpdateResponse;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        topologyProvider = new TopologyService(profile, topologyRegistry);
    }

    @Test
    public void getReceiveRoutesReturnsEmptyListOnRegistryEmptyList() {
        doAnswer(new Answer<List<Route>>() {

            @Override
            public List<Route> answer(InvocationOnMock invocation) throws Throwable {
                return Collections.emptyList();
            }
        }).when(topologyRegistry).getReceiveRoutes(anyString());
        assertTrue(topologyProvider.getReceiveRoutes(routingKey).size() == 0);
    }

    @Test
    public void getReceiveRoutesReturnsMultipleEntriesOnRegistryMultipleEntries() {
        doAnswer(new Answer<List<Route>>() {

            @Override
            public List<Route> answer(InvocationOnMock invocation) throws Throwable {
                return new ArrayList<Route>() {

                    {
                        add(route);
                        add(route);
                    }
                };
            }
        }).when(topologyRegistry).getReceiveRoutes(anyString());
        assertTrue(topologyProvider.getReceiveRoutes(routingKey).size() == 2);
    }

    @Test
    public void getReceiveRoutesReturnsSingleEntryOnRegistrySingleEntry() {
        doAnswer(new Answer<List<Route>>() {

            @Override
            public List<Route> answer(InvocationOnMock invocation) throws Throwable {
                return new ArrayList<Route>() {

                    {
                        add(route);
                    }
                };
            }
        }).when(topologyRegistry).getReceiveRoutes(anyString());
        assertTrue(topologyProvider.getReceiveRoutes(routingKey).size() == 1);
    }

    @Test(expected = NullPointerException.class)
    public void getReceiveRoutesThrowsOnRegistryNull() {
        doAnswer(new Answer<List<Route>>() {

            @Override
            public List<Route> answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(topologyRegistry).getReceiveRoutes(anyString());
        topologyProvider.getReceiveRoutes(routingKey);
    }

    @Test
    public void getSendRoutesReturnsEmptyListOnRegistryEmptyList() {
        doAnswer(new Answer<List<Route>>() {

            @Override
            public List<Route> answer(InvocationOnMock invocation) throws Throwable {
                return Collections.emptyList();
            }
        }).when(topologyRegistry).getSendRoutes(anyString());
        assertTrue(topologyProvider.getSendRoutes(routingKey).size() == 0);
    }

    @Test(expected = NullPointerException.class)
    public void getSendRoutesReturnsEmptyListOnRegistryNull() {
        doAnswer(new Answer<List<Route>>() {

            @Override
            public List<Route> answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(topologyRegistry).getSendRoutes(anyString());
        topologyProvider.getSendRoutes(routingKey);
    }

    @Test
    public void getSendRoutesReturnsMultipleEntriesOnRegistryMultipleEntries() {
        doAnswer(new Answer<List<Route>>() {

            @Override
            public List<Route> answer(InvocationOnMock invocation) throws Throwable {
                return new ArrayList<Route>() {

                    {
                        add(route);
                        add(route);
                    }
                };
            }
        }).when(topologyRegistry).getSendRoutes(anyString());
        assertTrue(topologyProvider.getSendRoutes(routingKey).size() == 2);
    }

    @Test
    public void getSendRoutesReturnsSingleEntryOnRegistrySingleEntry() {
        doAnswer(new Answer<List<Route>>() {

            @Override
            public List<Route> answer(InvocationOnMock invocation) throws Throwable {
                return new ArrayList<Route>() {

                    {
                        add(route);
                    }
                };
            }
        }).when(topologyRegistry).getSendRoutes(anyString());
        assertTrue(topologyProvider.getSendRoutes(routingKey).size() == 1);
    }

    public void setEventBus(IEventBus eventBus) {
        eventBus.subscribe(new IEventHandler<TopologyUpdateResponse>() {

            @Override
            public Class<TopologyUpdateResponse> getEventType() {
                return TopologyUpdateResponse.class;
            }

            @Override
            public Object handle(TopologyUpdateResponse event, Map<String, String> headers) {
                topologyRegistry = event.getTopologyRegistry();

                return null;
            }

            @Override
            public Object handleFailed(Envelope envelope, Exception e) {
                return null;
            }
        });
    }

    @Test
    public void setEventBusSubscribes() {
        topologyProvider.setEventBus(eventBus);
        verify(eventBus).subscribe(any(IEventHandler.class));
    }

    @Test
    public void topologyUpdateUpdatesTopologyRegistry() {
        doAnswer(new Answer<ITopologyRegistry>() {

            @Override
            public ITopologyRegistry answer(InvocationOnMock invocation) throws Throwable {
                return topologyRegistry2;
            }
        }).when(topologyUpdateResponse).getTopologyRegistry();

        doAnswer(new Answer<List<Route>>() {

            @Override
            public List<Route> answer(InvocationOnMock invocation) throws Throwable {
                return new ArrayList<Route>() {

                    {
                        add(route);
                        add(route);
                        add(route);
                    }
                };
            }
        }).when(topologyRegistry2).getReceiveRoutes(anyString());
        ArgumentCaptor<IEventHandler> eventHandlerCaptor = ArgumentCaptor.forClass(IEventHandler.class);
        topologyProvider.setEventBus(eventBus);
        verify(eventBus).subscribe(eventHandlerCaptor.capture());
        eventHandlerCaptor.getValue().handle(topologyUpdateResponse, null);
        assertTrue(topologyProvider.getReceiveRoutes(routingKey).size() == 3);
    }
}

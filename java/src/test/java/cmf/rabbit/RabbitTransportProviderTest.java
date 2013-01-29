package cmf.rabbit;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;
import cmf.bus.berico.IEnvelopeReceivedCallback;
import cmf.rabbit.topology.Exchange;
import cmf.rabbit.topology.ITopologyService;
import cmf.rabbit.topology.RouteInfo;
import cmf.rabbit.topology.RoutingInfo;

public class RabbitTransportProviderTest {

	@Test
	@SuppressWarnings("unchecked")
	public void send_declares_the_exchange_and_publishes_the_message() throws Exception {
		
		// Ton of setup.
		
		ITopologyService topologyService = mock(ITopologyService.class);
		
		RoutingInfo routingInfo = mock(RoutingInfo.class);
		
		RouteInfo routeInfo = mock(RouteInfo.class);
		
		Exchange exchange = mock(Exchange.class);
		
		when(routeInfo.getProducerExchange()).thenReturn(exchange);
		
		List<RouteInfo> routes = Arrays.asList(new RouteInfo[]{ routeInfo });
		
		when(routingInfo.getRoutes()).thenReturn(routes);
		
		when(topologyService.getRoutingInfo(any(Map.class))).thenReturn(routingInfo);
		
		IRabbitChannelFactory channelFactory = mock(IRabbitChannelFactory.class);
		
		Channel channel = mock(Channel.class);
		
		when(channelFactory.getChannelFor(exchange)).thenReturn(channel);
		
		RabbitTransportProvider transportProvider 
			= spy(new RabbitTransportProvider(topologyService, channelFactory));
		
		transportProvider.listeners = spy(transportProvider.listeners);
		
		Envelope envelope = mock(Envelope.class);
		
		Map<String, String> headers = new HashMap<String, String>();
		
		when(envelope.getHeaders()).thenReturn(headers);
		
		transportProvider.send(envelope);
		
		// Now test.
		
		verify(topologyService).getRoutingInfo(headers);
		
		verify(routingInfo).getRoutes();
		
		verify(routeInfo).getProducerExchange();
		
		verify(channelFactory).getChannelFor(exchange);
		
		verify(channel).exchangeDeclare(anyString(), anyString(), anyBoolean(), anyBoolean(), any(Map.class));
		
		byte[] prototype = new byte[0];
		
		verify(channel).basicPublish(anyString(), anyString(), any(AMQP.BasicProperties.class), any(prototype.getClass()));
	}

	
	@Test
	@SuppressWarnings("unchecked")
	public void register_correctly_creates_a_RabbitListener_and_adds_it_to_the_pool_of_listeners() throws Exception {
		
		// Ton of setup.
		
		ITopologyService topologyService = mock(ITopologyService.class);
		
		RoutingInfo routingInfo = mock(RoutingInfo.class);
		
		RouteInfo routeInfo = mock(RouteInfo.class);
		
		Exchange exchange = mock(Exchange.class);
		
		when(routeInfo.getConsumerExchange()).thenReturn(exchange);
		
		List<RouteInfo> routes = Arrays.asList(new RouteInfo[]{ routeInfo });
		
		when(routingInfo.getRoutes()).thenReturn(routes);
		
		when(topologyService.getRoutingInfo(any(Map.class))).thenReturn(routingInfo);
		
		IRabbitChannelFactory channelFactory = mock(IRabbitChannelFactory.class);
		
		Channel channel = mock(Channel.class);
		
		when(channelFactory.getChannelFor(exchange)).thenReturn(channel);
		
		RabbitTransportProvider transportProvider 
			= spy(new RabbitTransportProvider(topologyService, channelFactory));
		
		transportProvider.listeners = spy(transportProvider.listeners);
		
		RabbitListener listener = mock(RabbitListener.class);
		
		when(transportProvider.getListener(
			any(IRegistration.class), any(Exchange.class))).thenReturn(listener);
		
		IRegistration registration = mock(IRegistration.class);
		
		transportProvider.register(registration);
		
		// Now test.
		
		verify(topologyService).getRoutingInfo(registration.getRegistrationInfo());
		
		verify(routingInfo).getRoutes();
		
		verify(routeInfo).getConsumerExchange();
		
		verify(channelFactory).getChannelFor(exchange);
		
		verify(transportProvider).getListener(registration, exchange);
		
		verify(listener).onClose(any(IListenerCloseCallback.class));
		
		verify(listener, atMost(1)).onConnectionError(isA(ReconnectOnConnectionErrorCallback.class));
		
		verify(listener).onEnvelopeReceived(any(IEnvelopeReceivedCallback.class));
		
		verify(listener).start(channel);
		
		verify(transportProvider.listeners).put(registration, listener);
	}

	@Test
	public void unregister_stops_the_listener_and_removes_the_listener_from_the_listener_pool(){
		
		// Ton of setup.
		
		ITopologyService topologyService = mock(ITopologyService.class);
		
		IRabbitChannelFactory channelFactory = mock(IRabbitChannelFactory.class);
		
		RabbitTransportProvider transportProvider 
			= spy(new RabbitTransportProvider(topologyService, channelFactory));
		
		transportProvider.listeners = spy(transportProvider.listeners);
		
		RabbitListener listener = mock(RabbitListener.class);
		
		IRegistration registration = mock(IRegistration.class);
		
		transportProvider.listeners.put(registration, listener);
		
		transportProvider.unregister(registration);
		
		verify(transportProvider.listeners).remove(registration);
		
		verify(listener).stopListening();
	}
	
}

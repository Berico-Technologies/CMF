package cmf.rabbit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.ConnectException;

import org.junit.Test;
import org.mockito.InOrder;

import cmf.rabbit.topology.Exchange;

import com.rabbitmq.client.Channel;

public class ConnectionRemediatorTest {

	@Test
	public void connectionRemediator_attempts_to_reconnect_listener_to_broker() throws Exception {
		
		Channel channel = mock(Channel.class);
		
		Exchange exchange = mock(Exchange.class);
		
		RabbitListener listener = mock(RabbitListener.class);
		
		when(listener.getExchange()).thenReturn(exchange);
		
		IRabbitChannelFactory channelFactory = mock(IRabbitChannelFactory.class);
		
		when(channelFactory.getChannelFor(exchange)).thenReturn(channel);
		
		ConnectionRemediator connectionRemediator 
			= spy(new ConnectionRemediator(listener, channelFactory));
		
		connectionRemediator.run();
		
		InOrder inorder = inOrder(listener, channelFactory, connectionRemediator);
		
		inorder.verify(channelFactory).getChannelFor(exchange);
		
		inorder.verify(listener).start(channel);
		
		inorder.verify(connectionRemediator).stopTrying();
	}
	
	@Test
	public void connectionRemediator_cycles_until_reconnection() throws Exception {
		
		Channel channel = mock(Channel.class);
		
		Exchange exchange = mock(Exchange.class);
		
		RabbitListener listener = mock(RabbitListener.class);
		
		when(listener.getExchange()).thenReturn(exchange);
		
		IRabbitChannelFactory channelFactory = mock(IRabbitChannelFactory.class);
		
		// Throw three consecutive exceptions, then return a working channel.
		when(channelFactory.getChannelFor(exchange))
			.thenThrow(new ConnectException())
			.thenThrow(new ConnectException())
			.thenThrow(new ConnectException())
			.thenReturn(channel);
		
		ConnectionRemediator connectionRemediator 
			= spy(new ConnectionRemediator(listener, channelFactory, 100));
		
		InOrder inorder = inOrder(listener, channelFactory, connectionRemediator);
		
		try {
		
			connectionRemediator.run();
			
			inorder.verify(channelFactory).getChannelFor(exchange);
			
			inorder.verify(connectionRemediator).nap();
		
			inorder.verify(channelFactory).getChannelFor(exchange);
			
			inorder.verify(connectionRemediator).nap();
			
			inorder.verify(channelFactory).getChannelFor(exchange);
			
			inorder.verify(connectionRemediator).nap();
			
		} catch (Exception ex){
			
			assertTrue(ConnectException.class.isAssignableFrom(ex.getClass()));
		}
		
		inorder.verify(channelFactory).getChannelFor(exchange);
		
		inorder.verify(listener).start(channel);
		
		inorder.verify(connectionRemediator).stopTrying();
	}
}

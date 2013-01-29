package cmf.rabbit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;

import com.rabbitmq.client.Connection;

import cmf.rabbit.topology.Exchange;
import cmf.test.RequireProperties;
import cmf.test.TestProperties;

public class BasicChannelFactoryTest {

	@Rule
	public TestProperties testProperties = new TestProperties();
	
	@Test
	@RequireProperties({
		"cmf.connection.host",
		"cmf.connection.port",
		"cmf.connection.vhost",
		"cmf.connection.username", 
		"cmf.connection.password"
	})
	@SuppressWarnings("rawtypes")
	public void getConnection_returns_an_RabbitMQ_channel() throws IOException {
		
		Exchange exchange = new Exchange(
			"cmf", 
			testProperties.get("cmf.connection.host"), 
			testProperties.get("cmf.connection.vhost"), 
			testProperties.getInt("cmf.connection.port"),
			"cmf.test.BasicChannelFactoryTest", UUID.randomUUID().toString(), 
			"topic", false, true, new HashMap());
		
		exchange = spy(exchange);
		
		BasicChannelFactory channelFactory = new BasicChannelFactory(
				testProperties.get("cmf.connection.username"), 
				testProperties.get("cmf.connection.password"));
		
		Connection connection = channelFactory.getConnection(exchange);
		
		verify(exchange).getHostName();
		verify(exchange).getPort();
		verify(exchange).getVirtualHost();
		
		assertNotNull(connection);
	}

}

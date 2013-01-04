package cmf.rabbit;

import java.io.IOException;

import cmf.rabbit.topology.Exchange;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class BasicChannelFactory extends BaseChannelFactory {

	protected String username;
	protected String password;
	
	public BasicChannelFactory(String username, String password) {
		
		super();
		
		this.username = username;
		this.password = password;
	}

	public BasicChannelFactory(String username, String password, ChannelEqualityStrategy strategy) {
		
		super(strategy);
		
		this.username = username;
		this.password = password;
	}

	@Override
	public Connection getConnection(Exchange exchange) throws IOException {
		
		ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setHost(exchange.getHostName());
        factory.setPort(exchange.getPort());
        factory.setVirtualHost(exchange.getVirtualHost());
        
        return factory.newConnection();
	}
}
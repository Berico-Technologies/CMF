package cmf.rabbit;

import com.rabbitmq.client.Connection;

import cmf.rabbit.topology.Exchange;

public interface RabbitConnectionFactory {
	Connection connectTo(Exchange exchange);
}

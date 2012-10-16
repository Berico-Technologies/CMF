package cmf.bus.berico.rabbit;

import com.rabbitmq.client.Connection;

import cmf.bus.berico.rabbit.topology.Exchange;

public interface RabbitConnectionFactory {
	Connection ConnectTo(Exchange exchange);
}

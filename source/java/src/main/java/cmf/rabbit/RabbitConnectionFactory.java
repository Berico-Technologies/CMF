package cmf.rabbit;

import com.rabbitmq.client.Connection;

import cmf.bus.IDisposable;
import cmf.rabbit.topology.Exchange;

public interface RabbitConnectionFactory extends IDisposable {
	Connection connectTo(Exchange exchange) throws Exception;
}

package cmf.rabbit;

import cmf.bus.IDisposable;
import cmf.rabbit.topology.Exchange;

import com.rabbitmq.client.Connection;

public interface RabbitConnectionFactory extends IDisposable {

    Connection connectTo(Exchange exchange) throws Exception;
}

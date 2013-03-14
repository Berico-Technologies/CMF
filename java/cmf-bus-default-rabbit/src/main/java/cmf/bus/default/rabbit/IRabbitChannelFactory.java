package cmf.bus.default.rabbit;

import cmf.bus.IDisposable;
import cmf.bus.default.rabbit.topology.Exchange;

import com.rabbitmq.client.Channel;

public interface IRabbitChannelFactory extends IDisposable {

	Channel getChannelFor(Exchange exchange) throws Exception;
}

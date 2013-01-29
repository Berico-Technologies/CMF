package cmf.rabbit;

import cmf.bus.IDisposable;
import cmf.rabbit.topology.Exchange;

import com.rabbitmq.client.Channel;

public interface IRabbitChannelFactory extends IDisposable {

	Channel getChannelFor(Exchange exchange) throws Exception;
}

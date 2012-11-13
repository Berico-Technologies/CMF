package cmf.rabbit;

import cmf.rabbit.topology.Exchange;

import com.rabbitmq.client.Channel;

public interface RabbitChannelFactory {

	Channel getChannelFor(Exchange exchange) throws Exception;
}

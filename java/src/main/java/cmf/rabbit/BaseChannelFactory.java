package cmf.rabbit;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import cmf.rabbit.topology.Exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public abstract class BaseChannelFactory implements RabbitChannelFactory {

	protected ConcurrentHashMap<Exchange, Channel> pooledChannels = new ConcurrentHashMap<Exchange, Channel>();
	
	protected ChannelEqualityStrategy channelEqualityStrategy = new SameBrokerStrategy();
	
	public BaseChannelFactory() {}
	
	public BaseChannelFactory(ChannelEqualityStrategy channelEqualityStrategy) {
		
		this.channelEqualityStrategy = channelEqualityStrategy;
	}

	/**
	 * Derived classes have the sole responsibility of providing connections
	 * to this class (however that is done: username/password, certificates, etc.).
	 * 
	 * @return
	 */
	public abstract Connection getConnection(Exchange exchange) throws IOException;
	
	@Override
	public synchronized Channel getChannelFor(Exchange exchange) throws Exception {
		
		Channel channel = findMatchingChannel(exchange);
		
		if (channel == null){
		
			Connection connection = this.getConnection(exchange);
			
			channel = connection.createChannel();
			
			this.pooledChannels.put(exchange, channel);
		}
		
		return channel;
	}
	
	protected Channel findMatchingChannel(Exchange exchange){
		
		for (Entry<Exchange, Channel> kvp : this.pooledChannels.entrySet()){
			
			if (channelEqualityStrategy.useSameChannel(kvp.getKey(), exchange)){
				
				return kvp.getValue();
			}
		}
		return null;
	}
	
	
	/**
	 * This is a predicate to determine whether a Channel created for
	 * one exchange should be used by another exchange.
	 * 
	 * @author rclayton
	 */
	public interface ChannelEqualityStrategy {
		
		boolean useSameChannel(Exchange existingChannel, Exchange channelRequest);
	}
	
	/**
	 * In this strategy, we will create a Channel per exchange
	 * 
	 * @author rclayton
	 */
	public class SameExchangeStrategy implements ChannelEqualityStrategy {

		@Override
		public boolean useSameChannel(Exchange existingChannel,
				Exchange channelRequest) {
			
			return existingChannel.getName().equals(channelRequest.getName())
				&& existingChannel.getHostName().equals(channelRequest.getHostName())
				&& existingChannel.getVirtualHost().equals(channelRequest.getVirtualHost())
				&& existingChannel.getPort() == channelRequest.getPort();
		}
	}
	
	/**
	 * Create a channel per broker in this strategy.
	 * 
	 * @author rclayton
	 */
	public class SameBrokerStrategy implements ChannelEqualityStrategy {
		
		@Override
		public boolean useSameChannel(Exchange existingChannel,
				Exchange channelRequest) {
			
			return existingChannel.getHostName().equals(channelRequest.getHostName())
				&& existingChannel.getVirtualHost().equals(channelRequest.getVirtualHost())
				&& existingChannel.getPort() == channelRequest.getPort();
		}
	}
}

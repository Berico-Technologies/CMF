package cmf.rabbit;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.IDisposable;
import cmf.rabbit.topology.Exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * Provides a lot of the broiler-plate functionality most Rabbit Connection Factories
 * would need to function properly. This base implementation imposes only one responsibility
 * on deriving classes: that they create and supply the connection to the broker (which
 * is where most configuration like authentication is done). 
 *  
 * @author rclayton
 *
 */
public abstract class BaseChannelFactory implements IRabbitChannelFactory, IDisposable {

	private static final Logger logger = LoggerFactory.getLogger(BaseChannelFactory.class);

	public static int HEARTBEAT_INTERVAL = 2;

	protected ConcurrentHashMap<Exchange, Connection> pooledConnections = new ConcurrentHashMap<Exchange, Connection>();
	
	/**
	 * Create a new instance of the ChannelFactory using the "SameBrokerStrategy"
	 */
	public BaseChannelFactory() {}
	
	/**
	 * Convenience method to allow setting of the heartbeat interval via DI container.
	 * @param interval 
	 */
	public void setHeartbeatInterval(int interval){
		HEARTBEAT_INTERVAL = interval;
	}
	
	/**
	 * Derived classes have the sole responsibility of providing connections
	 * to this class (however that is done: username/password, certificates, etc.).
	 * 
	 * @return
	 */
	public abstract Connection getConnection(Exchange exchange) throws Exception;
	
	/**
	 * Get the corresponding channel for the supplied Exchange.
	 * This overload specifically pools channels depending on the 
	 * ChannelEqualityStrategy. 
	 * @param exchange Exchange configuration for the Channel
	 * @return an AMQP Channel
	 */
	@Override
	public synchronized Channel getChannelFor(Exchange exchange) throws Exception {
		
		logger.trace("Getting channel for exchange: {}", exchange);
		
		Connection connection = null;
		
		if (pooledConnections.containsKey(exchange)){
			
			connection = pooledConnections.get(exchange);
		}
		else {
			
			connection = this.getConnection(exchange);
			
			pooledConnections.put(exchange, connection);
		}
			
		connection.addShutdownListener(new RabbitConnectionShutdownListener(this, exchange));
			
		Channel channel = connection.createChannel();
		
		return channel;
	}
	
	public boolean removeConnection(Exchange exchange){
		
		Connection connection = pooledConnections.remove(exchange);
		
		return connection != null;
	}
	
	@Override
	public void dispose() {}
}

package cmf.rabbit;

import cmf.rabbit.topology.Exchange;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class BasicConnectionFactory implements RabbitConnectionFactory {

    protected String password;
    protected String username;

    public BasicConnectionFactory(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection connectTo(Exchange exchange) throws Exception {
    		
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setHost(exchange.getHostName());
        factory.setPort(exchange.getPort());
        factory.setVirtualHost(exchange.getVirtualHost());

        return factory.newConnection();
    }

    @Override
    public void dispose() {
        // nothing to do
    }

    @Override
    protected void finalize() {
        dispose();
    }
}

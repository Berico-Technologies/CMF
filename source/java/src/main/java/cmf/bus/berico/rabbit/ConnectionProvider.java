package cmf.bus.berico.rabbit;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class ConnectionProvider {

    private ConnectionFactory connectionFactory;

    public ConnectionProvider(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void closeConnection(Connection connection) {
        // closing the connection will automatically close all of the channels created on the connection
        try {
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing the rabbit connection and channels", e);
        }
    }

    public Connection newConnection() {
        try {
            return connectionFactory.newConnection();
        } catch (IOException e) {
            throw new RuntimeException("Error opening the rabbit connection", e);
        }
    }

    public Channel newChannel(Connection connection) {
        try {
            return connection.createChannel();
        } catch (IOException e) {
            throw new RuntimeException("Error opening rabbit command channel", e);
        }
    }
}

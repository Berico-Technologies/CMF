package cmf.bus.berico.rabbit;

import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cmf.rabbit.ConnectionFactory;
import cmf.rabbit.ConnectionProvider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class ConnectionProviderTest {

    @Mock
    private ConnectionFactory connectionFactory;
    @Mock
    private Connection connection;
    @Mock
    private Channel channel;

    private ConnectionProvider connectionProvider;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        connectionProvider = new ConnectionProvider(connectionFactory);
    }

    @Test
    public void closeConnectionCloses() throws IOException {
        connectionProvider.closeConnection(connection);
        verify(connection).close();
    }

    @Test
    public void newConnectionCallsNewConnection() throws IOException {
        connectionProvider.newConnection();
        verify(connectionFactory).newConnection();
    }

    @Test
    public void newChannelCallsCreateChannel() throws IOException {
        connectionProvider.newChannel(connection);
        verify(connection).createChannel();
    }
}

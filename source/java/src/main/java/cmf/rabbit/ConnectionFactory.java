package cmf.rabbit;

public class ConnectionFactory extends com.rabbitmq.client.ConnectionFactory {

    public static final String HOST = "cmf.bus.connection.host";
    public static final String PASSWORD = "cmf.bus.connection.password";
    public static final String PORT = "cmf.bus.connection.port";
    public static final String RETRY_TIMEOUT = "cmf.bus.connection.retryTimeout";
    public static final String USERNAME = "cmf.bus.connection.username";
    public static final String VHOST = "cmf.bus.connection.vhost";
}

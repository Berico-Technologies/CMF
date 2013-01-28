package cmf.rabbit.topology;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import cmf.bus.berico.EnvelopeHeaderConstants;

public class SimpleTopologyService implements ITopologyService {

    protected String clientProfile;
    protected String hostName;
    protected String name;
    protected boolean isDurable;
    protected boolean isAutodelete;
    protected String type;
    protected int port;
    protected String virtualHost;

    public SimpleTopologyService(String clientProfile, String name, String hostname, String vhost, int port) {

        this.clientProfile = StringUtils.isBlank(clientProfile) ? UUID.randomUUID().toString() : clientProfile;
        this.name = StringUtils.isBlank(name) ? "cmf.simple.exchange" : name;
        hostName = StringUtils.isBlank(hostname) ? "localhost" : hostname;
        virtualHost = StringUtils.isBlank(vhost) ? "/" : vhost;
        this.port = port == 0 ? 5672 : port;
        type = "direct";
        isDurable = false;
        isAutodelete = true;
    }

    @Override
    public void dispose() {
        // nothing to do
    }

    @Override
    protected void finalize() {
        dispose();
    }

    public String getClientProfile() {
        return clientProfile;
    }

    public String getHostName() {
        return hostName;
    }

    public String getName() {
        return name;
    }

    public boolean isDurable() {
		return isDurable;
	}

	public boolean isAutodelete() {
		return isAutodelete;
	}

	public String getType() {
        return type;
    }

    public int getPort() {
        return port;
    }

    @Override
    public RoutingInfo getRoutingInfo(Map<String, String> headers) {
        String topic = headers.get(EnvelopeHeaderConstants.MESSAGE_TOPIC);

        Exchange theOneExchange = new Exchange(name, // exchange name
                        hostName, // host name
                        virtualHost, // virtual host
                        port, // port
                        topic, // routing key
                        String.format("%s#%s", clientProfile, topic), // queue name
                        type, // exchange type
                        isDurable, // is durable
                        isAutodelete, // is auto-delete
                        null); // arguments

        RouteInfo theOneRoute = new RouteInfo(theOneExchange, theOneExchange);

        List<RouteInfo> routingInfo = new ArrayList<RouteInfo>();
        routingInfo.add(theOneRoute);

        return new RoutingInfo(routingInfo);
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setClientProfile(String clientProfile) {
        this.clientProfile = clientProfile;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

	public void setDurable(boolean isDurable) {
		this.isDurable = isDurable;
	}

	public void setAutodelete(boolean isAutodelete) {
		this.isAutodelete = isAutodelete;
	}

	public void setPort(int port) {
        this.port = port;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }
}

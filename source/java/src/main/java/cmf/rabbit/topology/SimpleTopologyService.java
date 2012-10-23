package cmf.rabbit.topology;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import cmf.bus.berico.EnvelopeHeaderConstants;

public class SimpleTopologyService implements ITopologyService {

    protected String clientProfile;
    protected String name;
    protected String hostName;
    protected String virtualHost;
    protected int port;
    

    public String getClientProfile() { return clientProfile; }
	public void setClientProfile(String clientProfile) { this.clientProfile = clientProfile; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getHostName() { return hostName; }
	public void setHostName(String hostName) { this.hostName = hostName; }

	public String getVirtualHost() { return virtualHost;  }
	public void setVirtualHost(String virtualHost) { this.virtualHost = virtualHost; }

	public int getPort() { return port; }
	public void setPort(int port) { this.port = port; }

	
	public SimpleTopologyService(String clientProfile, String name, String hostname, String vhost, int port) {
	
		this.clientProfile = StringUtils.isBlank(clientProfile) ? UUID.randomUUID().toString() : clientProfile;
		this.name = StringUtils.isBlank(name) ? "cmf.simple.exchange" : name;
        this.hostName = StringUtils.isBlank(hostname) ? "localhost" : hostname;
        this.virtualHost = StringUtils.isBlank(vhost) ? "/" : vhost;
        this.port = (port == 0) ? 5672 : port;
	}

	
	@Override
	public RoutingInfo getRoutingInfo(Map<String, String> headers) {
        String topic = headers.get(EnvelopeHeaderConstants.MESSAGE_TOPIC);
        
        Exchange theOneExchange = new Exchange(
            this.name, // exchange name
            this.hostName, // host name
            this.virtualHost, // virtual host
            this.port, // port 
            topic, // routing key
            String.format("%s#%s", this.clientProfile, topic), // queue name 
            "direct", // exchange type
            false, // is durable
            true, // is auto-delete
            null); // arguments

        RouteInfo theOneRoute = new RouteInfo(theOneExchange, theOneExchange);

        List<RouteInfo> routingInfo = new ArrayList<RouteInfo>();
        routingInfo.add(theOneRoute);
        
        return new RoutingInfo(routingInfo);
	}
	
	@Override
	public void dispose() {
		// nothing to do
	}
	
	@Override
	protected void finalize() {
		this.dispose();
	}
}

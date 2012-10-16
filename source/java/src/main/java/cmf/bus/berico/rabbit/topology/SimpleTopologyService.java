package cmf.bus.berico.rabbit.topology;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import cmf.bus.Envelope;
import cmf.bus.berico.rabbit.topology.ITopologyProvider;
import cmf.bus.eventing.IEventBus;
import cmf.bus.eventing.IEventHandler;

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
	}

	
	@Override
	public RoutingInfo getRoutingInfo(Map<String, String> headers) {
		// TODO Auto-generated method stub
		return null;
	}
}

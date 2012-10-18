package cmf.rabbit.topology;

import java.util.Map;

public class Exchange {
	
	protected String name;
	protected String hostName;
	protected String virtualHost;
	protected int port;
	protected String routingKey;
	protected String queueName;
	protected String exchangeType;
	protected boolean isDurable;
	protected boolean isAutoDelete;
	@SuppressWarnings("rawtypes")
	protected Map arguments;
	
	
	public String getName() { return name; }
	protected void setName(String name) { this.name = name; }
	
	public String getHostName() { return hostName; }
	protected void setHostName(String hostName) { this.hostName = hostName; }
	
    public String getVirtualHost() { return virtualHost; }
    protected void setVirtualHost(String virtualHost) { this.virtualHost = virtualHost; }
    
    public int getPort() { return port; }
    protected void setPort(int port) { this.port = port; }
    
    public String getRoutingKey() { return routingKey; }
    protected void setRoutingKey(String routingKey) { this.routingKey = routingKey; }
    
    public String getQueueName() { return queueName; }
    protected void setQueueName(String queueName) { this.queueName = queueName; }
    
    public String getExchangeType() { return exchangeType; }
    protected void setExchangeType(String exchangeType) { this.exchangeType = exchangeType; }
    
    public boolean getIsDurable() { return isDurable; }
    protected void setIsDurable(boolean isDurable) { this.isDurable = isDurable; }
    
    public boolean getIsAutoDelete() { return isAutoDelete; }
    protected void setIsAutoDelete(boolean isAutoDelete) { this.isAutoDelete = isAutoDelete; }
    
    @SuppressWarnings("rawtypes")
	public Map getArguments() { return arguments; }
    @SuppressWarnings("rawtypes")
	protected void setArguments(Map arguments) { this.arguments = arguments; }


    @SuppressWarnings("rawtypes")
	public Exchange(
        String name, 
        String hostName, 
        String vHost, 
        int port, 
        String routingKey, 
        String queueName, 
        String exchangeType, 
        boolean isDurable, 
        boolean autoDelete, 
        Map arguments)
    {
        this.name = name;
        this.hostName = hostName;
        this.virtualHost = vHost;
        this.port = port;
        this.routingKey = routingKey;
        this.queueName = queueName;
        this.exchangeType = exchangeType;
        this.isDurable = isDurable;
        this.isAutoDelete = autoDelete;
        this.arguments = arguments;
    }

    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append(String.format("Name:%s,", this.name));
        sb.append(String.format("HostName:{0},", this.hostName));
        sb.append(String.format("VirtualHost:{0},", this.virtualHost));
        sb.append(String.format("Port:{0},", this.port));
        sb.append(String.format("RoutingKey:{0},", this.routingKey));
        sb.append(String.format("QueueName:{0},", this.queueName));
        sb.append(String.format("ExchangeType:{0},", this.exchangeType));
        sb.append(String.format("IsDurable:{0},", this.isDurable));
        sb.append(String.format("IsAutoDelete:{0},", this.isAutoDelete));

        sb.append("}");
        return sb.toString();
    }
}

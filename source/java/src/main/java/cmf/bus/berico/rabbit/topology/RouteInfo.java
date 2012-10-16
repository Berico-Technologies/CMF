package cmf.bus.berico.rabbit.topology;

public class RouteInfo {
	
	protected Exchange producerExchange;
	protected Exchange consumerExchange;
	
	
	public Exchange getProducerExchange() { return this.producerExchange; }
	protected void setProducerExchange(Exchange exchange) { this.producerExchange = exchange; }
	
	public Exchange getConsumerExchange() { return this.consumerExchange; }
	protected void setConsumerExchange(Exchange exchange) { this.consumerExchange = exchange; }
	
	
	public RouteInfo(Exchange producerExchange, Exchange consumerExchange) {
        this.producerExchange = producerExchange;
        this.consumerExchange = consumerExchange;
	}
}

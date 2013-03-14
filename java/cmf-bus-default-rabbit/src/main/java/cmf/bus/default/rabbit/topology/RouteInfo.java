package cmf.bus.default.rabbit.topology;

public class RouteInfo {

    protected Exchange consumerExchange;
    protected Exchange producerExchange;

    public RouteInfo(Exchange producerExchange, Exchange consumerExchange) {
        this.producerExchange = producerExchange;
        this.consumerExchange = consumerExchange;
    }

    public Exchange getConsumerExchange() {
        return consumerExchange;
    }

    public Exchange getProducerExchange() {
        return producerExchange;
    }

    protected void setConsumerExchange(Exchange exchange) {
        consumerExchange = exchange;
    }

    protected void setProducerExchange(Exchange exchange) {
        producerExchange = exchange;
    }
}

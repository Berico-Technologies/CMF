package cmf.bus.rabbitmq.transport;

import org.apache.commons.lang.StringUtils;

public class Route {

    private String exchangeName;
    private String routingKey;

    public Route() {

    }

    public Route(String exchangeName, String routingKey) {
        setExchangeName(exchangeName);
        setRoutingKey(routingKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Route)) {
            return false;
        }
        Route other = (Route) obj;
        if (exchangeName == null) {
            if (other.exchangeName != null) {
                return false;
            }
        } else if (!exchangeName.equals(other.exchangeName)) {
            return false;
        }
        if (routingKey == null) {
            if (other.routingKey != null) {
                return false;
            }
        } else if (!routingKey.equals(other.routingKey)) {
            return false;
        }

        return true;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result += prime * (exchangeName == null ? 0 : exchangeName.hashCode());
        result += prime * (routingKey == null ? 0 : routingKey.hashCode());

        return result;
    }

    public void setExchangeName(String exchangeName) {
        if (StringUtils.isBlank(exchangeName)) {
            throw new IllegalArgumentException("Route exchange queueName cannot be set to null or an empty string");
        }
        this.exchangeName = exchangeName;
    }

    public void setRoutingKey(String routingKey) {
        if (StringUtils.isBlank(routingKey)) {
            throw new IllegalArgumentException("Route exchange routingKey cannot be set to null or an empty string");
        }
        this.routingKey = routingKey;
    }

    @Override
    public String toString() {
        return String.format("{\"exchangeName\":\"%s\", \"routingKey\"=\"%s\"}", exchangeName, routingKey);
    }

}

package cmf.bus.service.transport.rabbitmq;

import cmf.bus.core.IRoute;

public class Route implements IRoute {

    private String protocol;

    public Route() {

    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

}

package cmf.rabbit.topology;

public class RoutingInfo {

    protected Iterable<RouteInfo> routes;

    public RoutingInfo(Iterable<RouteInfo> routes) {
        this.routes = routes;
    }

    public Iterable<RouteInfo> getRoutes() {
        return routes;
    }

    protected void setRoutes(Iterable<RouteInfo> routes) {
        this.routes = routes;
    }
}

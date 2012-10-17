package cmf.rabbit.support;

import java.util.LinkedList;
import java.util.List;

import cmf.rabbit.Route;

public class InMemoryTopologyRegistry {

    private String receiveExchange;
    private String sendExchange;

    public InMemoryTopologyRegistry(String receiveExchange, String sendExchange) {
        this.receiveExchange = receiveExchange;
        this.sendExchange = sendExchange;
    }

    public List<Route> getReceiveRoutes(String routingKey) {
        List<Route> receiveRoutes = new LinkedList<Route>();
        receiveRoutes.add(new Route(receiveExchange, routingKey));

        return receiveRoutes;
    }

    public List<Route> getSendRoutes(String routingKey) {
        List<Route> sendRoutes = new LinkedList<Route>();
        sendRoutes.add(new Route(sendExchange, routingKey));

        return sendRoutes;
    }
}

package cmf.bus.rabbitmq.transport;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cmf.bus.rabbitmq.transport.Route;

public class RouteTest {

    private String exchangeName = "exchangeName";
    private Route route;
    private String routingKey = "routingKey";

    @Before
    public void before() {
        route = new Route(exchangeName, routingKey);
    }

    @Test
    public void identicalRoutesAreEqual() {
        Route duplicate = new Route(exchangeName, routingKey);
        assertEquals(route, duplicate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullExchangeNameThrows() {
        route.setExchangeName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRoutingKeyThrows() {
        route.setRoutingKey(null);
    }
    
}

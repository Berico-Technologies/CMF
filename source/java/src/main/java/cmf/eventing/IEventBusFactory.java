package cmf.eventing;

import java.util.Map;

public interface IEventBusFactory {

    IEventBus newEventBus(String clientName, Map<String, String> connectionParameters);
}

package cmf.bus.core.transport;

import java.util.Collection;

public interface ITopologyProvider {

    Collection<IRoute> getSendRoutes(String registrationKey);

    Collection<IRoute> getReceiveRoutes(String registrationKey);

}

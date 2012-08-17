package cmf.bus.core;

import java.util.Collection;


public interface ITopologyProvider extends IRegistrationHandler, ISendHandler {

    Collection<IRoute> getSendRoutes(String registrationKey);

    Collection<IRoute> getReceiveRoutes(String registrationKey);

}

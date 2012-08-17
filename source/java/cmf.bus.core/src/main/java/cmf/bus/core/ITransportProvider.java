package cmf.bus.core;


public interface ITransportProvider extends IRegistrationHandler, ISendHandler {

    boolean canSendToRoute(IRoute route);

    boolean canReceiveFromRoute(IRoute route);

}

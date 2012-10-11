package cmf.bus.eventing.rpc;

public interface IRpcReceiver {

    <T> T getResponseTo(Object request);

    <T> T getResponseTo(Object request, long timeout);
}

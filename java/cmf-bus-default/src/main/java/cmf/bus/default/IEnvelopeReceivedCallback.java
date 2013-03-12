package cmf.bus.default;

public interface IEnvelopeReceivedCallback {

    void handleReceive(IEnvelopeDispatcher envelope);
}

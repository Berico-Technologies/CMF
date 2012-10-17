package cmf.bus.berico;


public interface IEnvelopeReceivedCallback {

    void handleReceive(IEnvelopeDispatcher envelope);
}

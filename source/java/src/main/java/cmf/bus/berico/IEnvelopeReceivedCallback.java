package cmf.bus.berico;


public interface IEnvelopeReceivedCallback {

    Object handleReceive(IEnvelopeDispatcher envelope);
}

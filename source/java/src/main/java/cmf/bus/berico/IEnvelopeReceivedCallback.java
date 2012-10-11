package cmf.bus.berico;

import cmf.bus.Envelope;


public interface IEnvelopeReceivedCallback {

    Object handleReceive(Envelope envelope);
}

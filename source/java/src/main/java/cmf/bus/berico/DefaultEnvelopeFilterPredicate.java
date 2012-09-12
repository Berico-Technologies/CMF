package cmf.bus.berico;

import cmf.bus.Envelope;
import cmf.bus.IEnvelopeFilterPredicate;

public class DefaultEnvelopeFilterPredicate implements IEnvelopeFilterPredicate {

    @Override
    public boolean filter(Envelope envelope) {
        return true;
    }
}

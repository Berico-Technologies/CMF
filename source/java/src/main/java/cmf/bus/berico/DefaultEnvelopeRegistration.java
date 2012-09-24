package cmf.bus.berico;

import java.util.HashMap;
import java.util.Map;

import cmf.bus.IEnvelopeFilterPredicate;
import cmf.bus.IEnvelopeHandler;
import cmf.bus.IRegistration;

public class DefaultEnvelopeRegistration implements IRegistration {

    protected IEnvelopeHandler envelopeHandler = new NullEnvelopeHandler();
    protected IEnvelopeFilterPredicate filterPredicate = new DefaultEnvelopeFilterPredicate();
    protected Map<String, String> registrationInfo = new HashMap<String, String>();

    @Override
    public IEnvelopeHandler getEnvelopeHandler() {
        return envelopeHandler;
    }

    @Override
    public IEnvelopeFilterPredicate getFilterPredicate() {
        return filterPredicate;
    }

    @Override
    public Map<String, String> getRegistrationInfo() {
        return registrationInfo;
    }

    @Override
    public void setEnvelopeHandler(IEnvelopeHandler envelopeHandler) {
        this.envelopeHandler = envelopeHandler;
    }

    @Override
    public void setFilterPredicate(IEnvelopeFilterPredicate filterPredicate) {
        this.filterPredicate = filterPredicate;
    }

    @Override
    public void setRegistrationInfo(Map<String, String> registrationInfo) {
        this.registrationInfo = registrationInfo;
    }
}
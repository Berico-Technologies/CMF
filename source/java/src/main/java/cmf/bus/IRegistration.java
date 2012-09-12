package cmf.bus;

import java.util.Map;

public interface IRegistration {

    IEnvelopeHandler getEnvelopeHandler();

    IEnvelopeFilterPredicate getFilterPredicate();

    Map<String, String> getRegistrationInfo();

    void setEnvelopeHandler(IEnvelopeHandler envelopeHandler);

    void setFilterPredicate(IEnvelopeFilterPredicate envelopeFilterPredicate);

    void setRegistrationInfo(Map<String, String> registrationInfo);
}

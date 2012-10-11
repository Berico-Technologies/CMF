package cmf.bus;

import java.util.Map;

public interface IRegistration {

    IEnvelopeFilterPredicate getFilterPredicate();

    IEnvelopeHandler getHandler();

    Map<String, String> getRegistrationInfo();

    void setFilterPredicate(IEnvelopeFilterPredicate envelopeFilterPredicate);

    void setHandler(IEnvelopeHandler envelopeHandler);

    void setRegistrationInfo(Map<String, String> registrationInfo);
}

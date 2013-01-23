package cmf.bus;

import java.util.Map;

public interface IRegistration {

    IEnvelopeFilterPredicate getFilterPredicate();

    Map<String, String> getRegistrationInfo();

    Object handle(Envelope env) throws Exception;

    Object handleFailed(Envelope env, Exception ex) throws Exception;

    // IEnvelopeHandler getHandler();
    //
    // void setFilterPredicate(IEnvelopeFilterPredicate envelopeFilterPredicate);
    //
    // void setHandler(IEnvelopeHandler envelopeHandler);
    //
    // void setRegistrationInfo(Map<String, String> registrationInfo);
}

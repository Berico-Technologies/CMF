package cmf.bus.core;

public interface IRegistration {

    String getRegistrationKey();

    void setRegistrationKey(String registrationKey);

    IEnvelopeHandler getEnvelopeHandler();

    void setEnvelopeHandler(IEnvelopeHandler envelopeHandler);

}

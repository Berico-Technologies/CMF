package cmf.bus.core;

public interface IRegistration {

    IEnvelopeHandler getEnvelopeHandler();

    void setEnvelopeHandler(IEnvelopeHandler envelopeHandler);

    String getTopic();
    
    void setTopic(String topic);
    
}

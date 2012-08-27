package cmf.bus.pubsub;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.IEnvelopeHandler;
import cmf.bus.core.ITransportFilter;
import cmf.bus.pubsub.Registration;

public class RegistrationTest {

    @Mock
    private IEnvelope envelope;
    @Mock
    private IEnvelopeHandler envelopeHandler;
    @Mock
    private ITransportFilter transportFilter;
    private String topic = "topic";
    private String queueName = "queueName";
    private Registration registration;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        registration = new Registration();
        registration.setTopic(topic);
        registration.setQueueName(queueName);
        registration.setEnvelopeHandler(envelopeHandler);
        registration.setTransportFilter(transportFilter);
    }

    @Test
    public void identicalRegistrationsAreEqual() {
        Registration duplicate = new Registration();
        duplicate.setTopic(topic);
        duplicate.setQueueName(queueName);
        duplicate.setEnvelopeHandler(envelopeHandler);
        duplicate.setTransportFilter(transportFilter);
        assertEquals(registration, duplicate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingTopicToNullShouldThrow() {
        registration.setTopic(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingQueueNameToNullShouldThrow() {
        registration.setQueueName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingQueueNameToZeroLengthStringShouldThrow() {
        registration.setQueueName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingEnvelopeHandlerToNullShouldThrow() {
        registration.setEnvelopeHandler(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void settingTransportFilterToNullShouldThrow() {
        registration.setTransportFilter(null);
    }
    
}

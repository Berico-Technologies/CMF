package cmf.bus.eventing;
//package cmf.bus.eventing;
//
//import static org.junit.Assert.assertEquals;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import cmf.bus.Envelope;
//import cmf.bus.berico.IEnvelopeHandler;
//import cmf.bus.berico.Registration;
//
//public class RegistrationTest {
//
//    @Mock
//    private Envelope envelope;
//    @Mock
//    private IEnvelopeHandler envelopeHandler;
//    private String topic = "topic";
//    private Registration registration;
//
//    @Before
//    public void before() {
//        MockitoAnnotations.initMocks(this);
//
//        registration = new Registration();
//        registration.setTopic(topic);
//        registration.setEnvelopeHandler(envelopeHandler);
//    }
//
//    @Test
//    public void identicalRegistrationsAreEqual() {
//        Registration duplicate = new Registration();
//        duplicate.setTopic(topic);
//        duplicate.setEnvelopeHandler(envelopeHandler);
//        assertEquals(registration, duplicate);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void settingTopicToNullShouldThrow() {
//        registration.setTopic(null);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void settingEnvelopeHandlerToNullShouldThrow() {
//        registration.setEnvelopeHandler(null);
//    }
//    
//}

package cmf.bus.pubsub.event;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.IEnvelopeBus;
import cmf.bus.core.IRegistration;
import cmf.bus.core.event.IEventHandler;
import cmf.bus.core.serializer.ISerializer;

public class EventBusTest {

    @Mock
    private IEnvelope envelope;
    @Mock
    private IEnvelopeBus envelopeBus;
    private EventBus eventBus;
    @Mock
    private IRegistration registration;
    @Mock
    private IEventHandler<Object> eventHandler;
    @Mock
    private ISerializer serializer;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        doAnswer(new Answer<byte[]>() {

            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                byte[] serialized = {};
                
                return serialized;
            }
            
        }).when(serializer).byteSerialize(any(Object.class));
        eventBus = new EventBus();
        eventBus.setEnvelopeBus(envelopeBus);
        eventBus.setSerializer(serializer);
    }
    
    @Test
    public void registerCallsEnvelopeBusRegister() {
        eventBus.register(eventHandler, Object.class);
        verify(envelopeBus).register(any(IRegistration.class));
    }

    @Test
    public void sendCallsEnvelopeBusSend() {
        eventBus.send(new Object());
        verify(envelopeBus).send(any(IEnvelope.class));
    }
    
    @Test(expected = RuntimeException.class)
    public void envelopeBusSendExceptionThrows() {
        doThrow(RuntimeException.class).when(envelopeBus).send(any(IEnvelope.class));
        eventBus.send(envelope);
    }

    @Test(expected = RuntimeException.class)
    public void envelopeBusRegisterExceptionThrows() {
        doThrow(RuntimeException.class).when(envelopeBus).register(any(IRegistration.class));
        eventBus.register(eventHandler, Object.class);
    }

}

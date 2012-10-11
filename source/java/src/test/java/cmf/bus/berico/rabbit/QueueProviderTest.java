package cmf.bus.berico.rabbit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cmf.bus.IRegistration;
import cmf.bus.berico.IEnvelopeDispatcher;
import cmf.bus.berico.IEnvelopeReceivedCallback;
import cmf.bus.berico.rabbit.support.RabbitRegistrationHelper;

import com.rabbitmq.client.Channel;

@SuppressWarnings({"unchecked","serial"})
public class QueueProviderTest {

    @Mock
    private Channel channel;
    @Mock
    private IRegistration registration;
    @Mock
    private IEnvelopeDispatcher envelopeDispatcher;
    @Mock
    private IEnvelopeReceivedCallback callback;

    private QueueProvider queueProvider;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        queueProvider = new QueueProvider();
    }

    @Test
    public void newQueueGeneratesQueueName() throws IOException {
        queueProvider.newQueue(channel, registration, callback);
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(channel).queueDeclare(queueNameCaptor.capture(), anyBoolean(), anyBoolean(), anyBoolean(),
                        any(Map.class));
        assertNotNull(queueNameCaptor.getValue());
    }

    @Test
    public void newQueueDoesntOverwriteQueueName() throws IOException {
        final String queueName = "queueName1";
        doAnswer(new Answer<Map<String, String>>() {

            @Override
            public HashMap<String, String> answer(InvocationOnMock invocation) throws Throwable {
                return new HashMap<String, String>() {

                    {
                        put(RabbitRegistrationHelper.RegistrationInfo.QUEUE_NAME, queueName);
                    }
                };
            }
        }).when(registration).getRegistrationInfo();
        queueProvider.newQueue(channel, registration, callback);
        ArgumentCaptor<String> queueNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(channel).queueDeclare(queueNameCaptor.capture(), anyBoolean(), anyBoolean(), anyBoolean(),
                        any(Map.class));
        assertEquals(queueName, queueNameCaptor.getValue());
    }
}

// package cmf.bus.berico;
//
// import static org.mockito.Matchers.any;
// import static org.mockito.Matchers.eq;
// import static org.mockito.Matchers.anyMap;
// import static org.mockito.Mockito.doAnswer;
// import static org.mockito.Mockito.verify;
//
// import java.util.LinkedList;
// import java.util.List;
// import java.util.Map;
//
// import org.junit.Before;
// import org.junit.Test;
// import org.mockito.ArgumentCaptor;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.mockito.invocation.InvocationOnMock;
// import org.mockito.stubbing.Answer;
//
// import cmf.bus.Envelope;
// import cmf.bus.IEnvelopeHandler;
// import cmf.bus.IRegistration;
//
// @SuppressWarnings("unchecked")
// public class DefaultEnvelopeBusTest {
//
// @Mock
// private Map<String, Object> context;
// @Mock
// private Envelope envelope;
// private DefaultEnvelopeBus envelopeBus;
// @Mock
// private IInboundEnvelopeProcessor inboundEnvelopeProcessor;
// private List<IInboundEnvelopeProcessor> inboundProcessors = new LinkedList<IInboundEnvelopeProcessor>();
// @Mock
// private IOutboundEnvelopeProcessor outboundEnvelopeProcessor;
// private List<IOutboundEnvelopeProcessor> outboundProcessors = new LinkedList<IOutboundEnvelopeProcessor>();
// @Mock
// private IRegistration registration;
// @Mock
// private ITransportProvider transportProvider;
// @Mock
// private IEnvelopeHandler envelopeHandler;
// @Mock
// private IEnvelopeDispatcher envelopeDispatcher;
//
// @Before
// public void before() {
// MockitoAnnotations.initMocks(this);
//
// inboundProcessors.add(inboundEnvelopeProcessor);
// outboundProcessors.add(outboundEnvelopeProcessor);
// envelopeBus =
// new DefaultEnvelopeBus(transportProvider, inboundProcessors, outboundProcessors,
// envelopeDispatcher);
// }
//
// @Test
// public void registerCallsTransportProviderRegister() {
// envelopeBus.register(registration);
// verify(transportProvider).register(eq(registration), any(IEnvelopeReceivedCallback.class));
// }
//
// @Test
// public void unregisterCallsTransportProviderRegister() {
// envelopeBus.unregister(registration);
// verify(transportProvider).unregister(any(IRegistration.class));
// }
//
// @Test(expected = IllegalArgumentException.class)
// public void registerThrowsOnNullRegistration() {
// envelopeBus.register((IRegistration) null);
// }
//
// @Test(expected = IllegalArgumentException.class)
// public void unregisterThrowsOnNullRegistration() {
// envelopeBus.unregister((IRegistration) null);
// }
//
// @Test
// public void sendCallsOutboundProcessor() {
// envelopeBus.send(envelope);
// verify(outboundEnvelopeProcessor).processOutbound(any(Envelope.class), anyMap());
// }
//
// @Test
// public void sendCallsTransportProviderSend() {
// envelopeBus.send(envelope);
// verify(transportProvider).send(any(Envelope.class));
// }
//
// @Test(expected = RuntimeException.class)
// public void sendThrowsOnNullEnvelope() {
// envelopeBus.send((Envelope) null);
// }
//
// @Test
// public void receiveCallsInboundProcessor() {
// ArgumentCaptor<IEnvelopeReceivedCallback> callbackArgumentCaptor =
// ArgumentCaptor.forClass(IEnvelopeReceivedCallback.class);
// final Object result = null;
// doAnswer(new Answer<Object>() {
//
// @Override
// public Object answer(InvocationOnMock invocation) throws Throwable {
// return result;
// }
// }).when(envelopeDispatcher).dispatch(registration, envelope);
// envelopeBus.register(registration);
// verify(transportProvider).register(eq(registration), callbackArgumentCaptor.capture());
// callbackArgumentCaptor.getValue().handleReceive(envelope);
// verify(inboundEnvelopeProcessor).processInbound(any(Envelope.class), anyMap());
// }
// }

package cmf.bus.berico;

import static org.mockito.Mockito.*;

import org.junit.Test;

import cmf.bus.Envelope;

public class InboundEnvelopeProcessorCallbackTest {

	@Test
	public void processInbound_is_called_when_envelope_received() {
		
		DefaultEnvelopeBus envelopeBus = mock(DefaultEnvelopeBus.class);
		
		InboundEnvelopeProcessorCallback callback = new InboundEnvelopeProcessorCallback(envelopeBus);
		
		IEnvelopeDispatcher dispatcher = mock(IEnvelopeDispatcher.class);
		
		Envelope envelope = mock(Envelope.class);
		
		when(dispatcher.getEnvelope()).thenReturn(envelope);
		
		callback.handleReceive(dispatcher);
		
		verify(dispatcher).getEnvelope();
		
		verify(envelopeBus).processInbound(envelope);
	}
	
	@Test
	public void callback_does_not_dispatch_envelope_if_an_inbound_processor_fails(){
		
		DefaultEnvelopeBus envelopeBus = mock(DefaultEnvelopeBus.class);
		
		// Processor fails!
		when(envelopeBus.processInbound(any(Envelope.class))).thenReturn(false);
		
		InboundEnvelopeProcessorCallback callback = new InboundEnvelopeProcessorCallback(envelopeBus);
		
		IEnvelopeDispatcher dispatcher = mock(IEnvelopeDispatcher.class);
		
		Envelope envelope = mock(Envelope.class);
		
		when(dispatcher.getEnvelope()).thenReturn(envelope);
		
		callback.handleReceive(dispatcher);
		
		verify(dispatcher).getEnvelope();
		
		verify(envelopeBus).processInbound(envelope);
		
		// dispatch(envelope) should not occur
		verify(dispatcher, never()).dispatch(envelope);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void callback_calls_dispatchFailed_on_exception_in_inbound_processors(){
		
		DefaultEnvelopeBus envelopeBus = mock(DefaultEnvelopeBus.class);
		
		Envelope envelope = mock(Envelope.class);
		
		when(envelopeBus.processInbound(envelope)).thenThrow(Exception.class);
		
		InboundEnvelopeProcessorCallback callback = new InboundEnvelopeProcessorCallback(envelopeBus);
		
		IEnvelopeDispatcher dispatcher = mock(IEnvelopeDispatcher.class);
		
		when(dispatcher.getEnvelope()).thenReturn(envelope);
		
		callback.handleReceive(dispatcher);
		
		verify(dispatcher).getEnvelope();
		
		verify(envelopeBus).processInbound(envelope);
		
		verify(dispatcher, never()).dispatch(envelope);
		
		verify(dispatcher).dispatchFailed(eq(envelope), any(Exception.class));
	}
	
	@Test
	public void callback_calls_dispatchFailed_on_exception_when_dispatching(){
		
		DefaultEnvelopeBus envelopeBus = mock(DefaultEnvelopeBus.class);
		
		Envelope envelope = mock(Envelope.class);
		
		when(envelopeBus.processInbound(envelope)).thenReturn(true);
		
		InboundEnvelopeProcessorCallback callback = new InboundEnvelopeProcessorCallback(envelopeBus);
		
		IEnvelopeDispatcher dispatcher = mock(IEnvelopeDispatcher.class);
		
		when(dispatcher.getEnvelope()).thenReturn(envelope);
		
		doThrow(Exception.class).when(dispatcher).dispatch(envelope);
		
		callback.handleReceive(dispatcher);
		
		verify(dispatcher).getEnvelope();
		
		verify(envelopeBus).processInbound(envelope);
		
		verify(dispatcher).dispatch(envelope);
		
		verify(dispatcher).dispatchFailed(eq(envelope), any(Exception.class));
	}
	
	@Test
	public void callback_calls_dispatch_if_there_are_no_errors_and_processInbound_is_true(){
		
		DefaultEnvelopeBus envelopeBus = mock(DefaultEnvelopeBus.class);
		
		Envelope envelope = mock(Envelope.class);
		
		when(envelopeBus.processInbound(envelope)).thenReturn(true);
		
		InboundEnvelopeProcessorCallback callback = new InboundEnvelopeProcessorCallback(envelopeBus);
		
		IEnvelopeDispatcher dispatcher = mock(IEnvelopeDispatcher.class);
		
		when(dispatcher.getEnvelope()).thenReturn(envelope);
		
		callback.handleReceive(dispatcher);
		
		verify(dispatcher).getEnvelope();
		
		verify(envelopeBus).processInbound(envelope);
		
		verify(dispatcher).dispatch(envelope);
		
		verify(dispatcher, never()).dispatchFailed(eq(envelope), any(Exception.class));
	}
}

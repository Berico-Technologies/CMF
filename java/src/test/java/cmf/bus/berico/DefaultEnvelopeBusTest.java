package cmf.bus.berico;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.InOrder;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

public class DefaultEnvelopeBusTest {
	
	@Test
	public void intialize_registers_callback_with_transport_provider() {
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		new DefaultEnvelopeBus(transportProvider);
		
		verify(transportProvider).onEnvelopeReceived(
			any(InboundEnvelopeProcessorCallback.class));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void processor_order_is_preserved_by_processInbound() {
		
		IEnvelopeProcessor iep1 = mock(IEnvelopeProcessor.class);
		IEnvelopeProcessor iep2 = mock(IEnvelopeProcessor.class);
		IEnvelopeProcessor iep3 = mock(IEnvelopeProcessor.class);
		
		
		when(iep1.processInbound(any(Envelope.class), any(Map.class)))
			.thenReturn(true);
		
		when(iep2.processInbound(any(Envelope.class), any(Map.class)))
		.thenReturn(true);
		
		when(iep3.processInbound(any(Envelope.class), any(Map.class)))
		.thenReturn(true);
		
		InOrder inorder = inOrder(iep1, iep2, iep3);
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = 
			new DefaultEnvelopeBus(transportProvider, 
				Arrays.asList(
					new IInboundEnvelopeProcessor[]{ iep1, iep2, iep3 }),
				Arrays.asList(
					new IOutboundEnvelopeProcessor[0]));
		
		Envelope envelope = mock(Envelope.class);
		
		boolean wasCompleted = bus.processInbound(envelope);
		
		assertTrue(wasCompleted);
		
		Map<String, Object> contextExample = new HashMap<String, Object>();
		
		inorder.verify(iep1).processInbound(envelope, contextExample);
		inorder.verify(iep2).processInbound(envelope, contextExample);
		inorder.verify(iep3).processInbound(envelope, contextExample);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void inbound_processor_stops_processing_envelope_if_a_processor_fails(){
		
		IInboundEnvelopeProcessor iep1 = mock(IInboundEnvelopeProcessor.class);
		IInboundEnvelopeProcessor iep2 = mock(IInboundEnvelopeProcessor.class);
		IInboundEnvelopeProcessor iep3 = mock(IInboundEnvelopeProcessor.class);
		
		when(iep1.processInbound(any(Envelope.class), any(Map.class)))
			.thenReturn(true);
		
		// Fails to complete processing (returns false)
		when(iep2.processInbound(any(Envelope.class), any(Map.class)))
		.thenReturn(false);
		
		when(iep3.processInbound(any(Envelope.class), any(Map.class)))
		.thenReturn(true);
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = 
			new DefaultEnvelopeBus(transportProvider, 
				Arrays.asList(
					new IInboundEnvelopeProcessor[]{ iep1, iep2, iep3 }),
				Arrays.asList(
					new IOutboundEnvelopeProcessor[0]));
		
		Envelope envelope = mock(Envelope.class);
		
		boolean wasCompleted = bus.processInbound(envelope);
		
		assertFalse(wasCompleted);
		
		Map<String, Object> contextExample = new HashMap<String, Object>();
		
		verify(iep1).processInbound(envelope, contextExample);
		
		verify(iep2).processInbound(envelope, contextExample);
		
		verifyZeroInteractions(iep3);
	}
	
	
	@Test
	public void processor_order_is_preserved_by_processOutbound() {
		
		IOutboundEnvelopeProcessor oep1 = mock(IOutboundEnvelopeProcessor.class);
		IOutboundEnvelopeProcessor oep2 = mock(IOutboundEnvelopeProcessor.class);
		IOutboundEnvelopeProcessor oep3 = mock(IOutboundEnvelopeProcessor.class);
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = 
			new DefaultEnvelopeBus(transportProvider, 
				Arrays.asList(
					new IInboundEnvelopeProcessor[0]),
				Arrays.asList(
					new IOutboundEnvelopeProcessor[]{ oep1, oep2, oep3 }));
		
		InOrder inorder = inOrder(oep1, oep2, oep3);
		
		Envelope envelope = mock(Envelope.class);
		
		bus.processOutbound(envelope);
		
		Map<String, Object> contextExample = new HashMap<String, Object>();
		
		inorder.verify(oep1).processOutbound(envelope, contextExample);
		inorder.verify(oep2).processOutbound(envelope, contextExample);
		inorder.verify(oep3).processOutbound(envelope, contextExample);
	}

	@Test(expected=IllegalArgumentException.class)
	public void register_throws_exception_if_registration_is_null() throws Exception {
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = new DefaultEnvelopeBus(transportProvider);
		
		bus.register(null);
	}
	
	@Test
	public void register_passes_registration_object_to_transport_provider() throws Exception{
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = new DefaultEnvelopeBus(transportProvider);
		
		IRegistration registration = mock(IRegistration.class);
		
		bus.register(registration);
		
		verify(transportProvider).register(registration);
	}

	@Test(expected=IllegalArgumentException.class)
	public void send_throws_exception_if_envelope_is_null() throws Exception {
	
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = new DefaultEnvelopeBus(transportProvider);
		
		bus.send(null);
	}

	@Test
	public void send_passes_envelope_to_processOutbound_and_then_transport_provider() throws Exception{
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = spy(new DefaultEnvelopeBus(transportProvider));
		
		Envelope envelope = mock(Envelope.class);
		
		InOrder inorder = inOrder(bus, transportProvider);
		
		bus.send(envelope);
		
		inorder.verify(bus).processOutbound(envelope);
		
		inorder.verify(transportProvider).send(envelope);
	}

	@Test(expected=IllegalArgumentException.class)
	public void unregister_throws_exception_if_registration_is_null() throws Exception {
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = new DefaultEnvelopeBus(transportProvider);
		
		bus.unregister(null);
	}

	@Test
	public void unregister_passes_registration_to_transport_provider() throws Exception {
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = new DefaultEnvelopeBus(transportProvider);
		
		IRegistration registration = mock(IRegistration.class);
		
		bus.unregister(registration);
		
		verify(transportProvider).unregister(registration);
	}
	
	@Test
	public void tranport_provider_and_processors_are_disposed_when_bus_dispose_method_is_called(){
		
		IInboundEnvelopeProcessor iep1 = mock(IInboundEnvelopeProcessor.class);
		IInboundEnvelopeProcessor iep2 = mock(IInboundEnvelopeProcessor.class);
		IInboundEnvelopeProcessor iep3 = mock(IInboundEnvelopeProcessor.class);
		
		IOutboundEnvelopeProcessor oep1 = mock(IOutboundEnvelopeProcessor.class);
		IOutboundEnvelopeProcessor oep2 = mock(IOutboundEnvelopeProcessor.class);
		IOutboundEnvelopeProcessor oep3 = mock(IOutboundEnvelopeProcessor.class);
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = 
			new DefaultEnvelopeBus(transportProvider, 
				Arrays.asList(
					new IInboundEnvelopeProcessor[]{ iep1, iep2, iep3 }),
				Arrays.asList(
					new IOutboundEnvelopeProcessor[]{ oep1, oep2, oep3 }));
		
		InOrder inorder = inOrder(transportProvider, iep1, iep2, iep3, oep1, oep2, oep3);
		
		bus.dispose();
		
		inorder.verify(transportProvider).dispose();
		inorder.verify(iep1).dispose();
		inorder.verify(iep2).dispose();
		inorder.verify(iep3).dispose();
		inorder.verify(oep1).dispose();
		inorder.verify(oep2).dispose();
		inorder.verify(oep3).dispose();
	}
}

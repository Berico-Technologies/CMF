package cmf.bus.berico;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

public class DefaultEnvelopeBusTest {
	
	@Test
	public void intialize_registers_callback_with_transport_provider() {
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		new DefaultEnvelopeBus(transportProvider);
		
		verify(transportProvider).onEnvelopeReceived(
			any(IEnvelopeReceivedCallback.class));
	}

	@Test
	public void processor_order_is_preserved_by_processInbound() {
		
		// mock up some processors
		IEnvelopeProcessor iep1 = mock(IEnvelopeProcessor.class);
		IEnvelopeProcessor iep2 = mock(IEnvelopeProcessor.class);
		IEnvelopeProcessor iep3 = mock(IEnvelopeProcessor.class);
		
		iep1.processEnvelope(any(EnvelopeContext.class), any(IContinuationCallback.class));
		iep2.processEnvelope(any(EnvelopeContext.class), any(IContinuationCallback.class));
		iep3.processEnvelope(any(EnvelopeContext.class), any(IContinuationCallback.class));
		
		
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = new DefaultEnvelopeBus(transportProvider);
			new DefaultEnvelopeBus(transportProvider, 
				Arrays.asList(
					new IEnvelopeProcessor[]{ iep1, iep2, iep3 }),
				Arrays.asList(
					new IEnvelopeProcessor[0]));
		
		InOrder inorder = inOrder(iep1, iep2, iep3);
			
		try {
			bus.processEnvelope(
					mock(EnvelopeContext.class), 
					Arrays.asList(new IEnvelopeProcessor[]{ iep1, iep2, iep3 }),
					mock(IContinuationCallback.class));
		} catch (Exception e) {
			Assert.fail("ProcessEnvelope threw an exception: " + e.toString());
		}
		
		inorder.verify(iep1).processEnvelope(any(EnvelopeContext.class), any(IContinuationCallback.class));
		inorder.verify(iep2).processEnvelope(any(EnvelopeContext.class), any(IContinuationCallback.class));
		inorder.verify(iep3).processEnvelope(any(EnvelopeContext.class), any(IContinuationCallback.class));
	}

	@Test
	public void inbound_processor_stops_processing_envelope_if_a_processor_fails(){
		
		IEnvelopeProcessor iep1 = mock(IEnvelopeProcessor.class);
		IEnvelopeProcessor iep2 = mock(IEnvelopeProcessor.class);
		IEnvelopeProcessor iep3 = mock(IEnvelopeProcessor.class);
		
		List<IEnvelopeProcessor> processorChain = new LinkedList<IEnvelopeProcessor>();
		processorChain.add(iep1);
		processorChain.add(iep2);
		processorChain.add(iep3);
		
		// setup the first mock to call its continuation method
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Exception {
				Object[] args = invocation.getArguments();
				IContinuationCallback continuation = (IContinuationCallback)args[1];
				continuation.continueProcessing();
				return null;
			}})
			.when(iep1).processEnvelope(any(EnvelopeContext.class), any(IContinuationCallback.class));
		
		// setup the second mock to NOT call its continuation method
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				return null;
			}})
			.when(iep1).processEnvelope(any(EnvelopeContext.class), any(IContinuationCallback.class));
		
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = new DefaultEnvelopeBus(transportProvider);

		try {
			bus.processEnvelope(
					any(EnvelopeContext.class), 
					processorChain,
					new IContinuationCallback() {

						@Override
						public void continueProcessing() throws Exception {
							Assert.fail("The EnvelopeBus's processEnvelope method should not have called the continuation callback");
						}
						
					});
		} catch (Exception e) {
			Assert.fail("The EnvelopeBus threw an exception: " + e.toString());
		}
		
		verify(iep1).processEnvelope(any(EnvelopeContext.class), any(IContinuationCallback.class));
		verify(iep2).processEnvelope(any(EnvelopeContext.class), any(IContinuationCallback.class));
		verifyZeroInteractions(iep3);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
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
	public void send_passes_envelope_to_processEnvelope_and_then_transport_provider() throws Exception{
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = spy(new DefaultEnvelopeBus(transportProvider));
		
		Envelope envelope = mock(Envelope.class);
		
		InOrder inorder = inOrder(bus, transportProvider);
		
		bus.send(envelope);
		
		inorder.verify(bus).send(envelope);
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
		
		IEnvelopeProcessor iep1 = mock(IEnvelopeProcessor.class);
		IEnvelopeProcessor iep2 = mock(IEnvelopeProcessor.class);
		IEnvelopeProcessor iep3 = mock(IEnvelopeProcessor.class);
		
		IEnvelopeProcessor oep1 = mock(IEnvelopeProcessor.class);
		IEnvelopeProcessor oep2 = mock(IEnvelopeProcessor.class);
		IEnvelopeProcessor oep3 = mock(IEnvelopeProcessor.class);
		
		ITransportProvider transportProvider = mock(ITransportProvider.class);
		
		DefaultEnvelopeBus bus = 
			new DefaultEnvelopeBus(transportProvider, 
				Arrays.asList(
					new IEnvelopeProcessor[]{ iep1, iep2, iep3 }),
				Arrays.asList(
					new IEnvelopeProcessor[]{ oep1, oep2, oep3 }));
		
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

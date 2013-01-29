package cmf.rabbit;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.rabbitmq.client.Channel;

import cmf.bus.Envelope;
import cmf.bus.IRegistration;

public class RabbitEnvelopeDispatcherTest {

	@Test
	public void dispatch_fires_the_overloaded_dispatch_method_with_constructor_supplied_envelope() {
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		dispatcher.dispatch();
		
		verify(dispatcher).dispatch(envelope);
	}

	@Test
	public void dispatch_correctly_passes_envelope_to_registration_handler_and_fires_respondToMessage() throws Exception {
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		when(registration.handle(envelope)).thenReturn(null);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		dispatcher.dispatch(envelope);
		
		verify(registration).handle(envelope);
		
		verify(dispatcher).respondToMessage(null);
	}
	
	@Test
	public void dispatch_correctly_passes_envelope_to_registration_handler_and_fires_respondToMessage_with_delivery_outcome() throws Exception {
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		when(registration.handle(envelope)).thenReturn(DeliveryOutcomes.Acknowledge);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		dispatcher.dispatch(envelope);
		
		verify(registration).handle(envelope);
		
		verify(dispatcher).respondToMessage(DeliveryOutcomes.Acknowledge);
	}
	
	@Test
	public void dispatch_correctly_calls_dispatchFailed_on_exception_in_registration_handler() throws Exception {
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		doThrow(Exception.class).when(registration).handle(envelope);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		dispatcher.dispatch(envelope);
		
		verify(registration).handle(envelope);
		
		verify(dispatcher).dispatchFailed(eq(envelope), any(Exception.class));
	}
	
	@Test
	public void dispatchFailed_correctly_passes_envelope_and_exception_to_registration_fail_handler() throws Exception {
		
		Exception targetException = new Exception();
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		when(registration.handleFailed(envelope, targetException)).thenReturn(DeliveryOutcomes.Acknowledge);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		dispatcher.dispatchFailed(envelope, targetException);
		
		verify(registration).handleFailed(envelope, targetException);
		
		verify(dispatcher).respondToMessage(DeliveryOutcomes.Acknowledge);
	}
	
	@Test
	public void dispatchFailed_calls_respondToMessage_with_DeliveryOutcomes_exception_when_exception_thrown_by_registration() throws Exception {
		
		Exception targetException = new Exception();
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		doThrow(Exception.class).when(registration).handleFailed(envelope, targetException);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		dispatcher.dispatchFailed(envelope, targetException);
		
		verify(registration).handleFailed(envelope, targetException);
		
		verify(dispatcher).respondToMessage(DeliveryOutcomes.Exception);
	}

	@Test
	public void respondToMessage_calls_channel_ack_on_null_result_from_handler() throws IOException{
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		dispatcher.respondToMessage(null);
		
		verify(channel).basicAck(deliveryTag, false);
	}

	@Test
	public void respondToMessage_calls_channel_ack_on_DeliveryOutcomes_Acknowledge_result_from_handler() throws IOException{
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		dispatcher.respondToMessage(DeliveryOutcomes.Acknowledge);
		
		verify(channel).basicAck(deliveryTag, false);
	}
	
	@Test
	public void respondToMessage_calls_channel_ack_on_DeliveryOutcomes_Null_result_from_handler() throws IOException{
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		dispatcher.respondToMessage(DeliveryOutcomes.Null);
		
		verify(channel).basicAck(deliveryTag, false);
	}
	
	@Test
	public void respondToMessage_calls_channel_ack_on_non_DeliveryOutcomes_object_result_from_handler() throws IOException{
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		// Date being the random object
		dispatcher.respondToMessage(new Date());
		
		verify(channel).basicAck(deliveryTag, false);
	}
	
	@Test
	public void respondToMessage_calls_channel_reject_on_DeliveryOutcomes_Reject_result_from_handler() throws IOException{
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		dispatcher.respondToMessage(DeliveryOutcomes.Reject);
		
		verify(channel).basicReject(deliveryTag, false);
	}
	
	@Test
	public void respondToMessage_calls_channel_nack_on_DeliveryOutcomes_Exception_result_from_handler() throws IOException{
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		dispatcher.respondToMessage(DeliveryOutcomes.Exception);
		
		verify(channel).basicNack(deliveryTag, false, false);
	}
	
	@Test
	public void respondToMessage_catches_IOException_on_channel_failure() throws IOException{
		
		IRegistration registration = mock(IRegistration.class);
		
		Envelope envelope = mock(Envelope.class);
		
		Channel channel = mock(Channel.class);
		
		long deliveryTag = 1l;
		
		doThrow(IOException.class).when(channel).basicAck(deliveryTag, false);
		
		RabbitEnvelopeDispatcher dispatcher = 
			spy(new RabbitEnvelopeDispatcher(registration, envelope, channel, deliveryTag));
		
		dispatcher.respondToMessage(DeliveryOutcomes.Acknowledge);
		
		verify(channel).basicAck(deliveryTag, false);
	}
	
}

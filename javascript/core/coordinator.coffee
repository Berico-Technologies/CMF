_      = (require "underscore")._
async  = (require "async")
conf   = (require "./configuration")
ex     = (require "./exceptions")
logger = new ((require "winston").Logger)(conf.LogConf);

x = exports ? this

# CMF Coordinator implementation, responsible for coordinating the transimital and
# reception of messages on a CMF-compliant bus.
class x.Coordinator
	
	@NoOpTransportSender = 
		send: (envelope) -> null
	
	@NoOpTransportReceiver = 
		register: (registration, handler) -> null
		on_envelope_received: (preprocessor) -> null
	
	@defaults = 
		inbound_chain: []
		outbound_chain: []
		transport_sender: @NoOpTransportSender
		transport_receiver: @NoOpTransportReceiver
	
	# Initialize the Coordinator
	# --------------------------------------------------------------------------------
	# @param options.inbound_chain = An array of Envelope Processors, functions with
	#	the signature: (next_processor_fn, envelope, context), that process envelopes
	#	coming from the transport layer before being dispatched to a registered handler.
	# @param options.outbound_chain = An array of Envelope Processors, functions with
	#	the signature: (next_processor_fn, envelope, context), that process envelopes
	#	before being handed-off to the transport layer for delivery.
	# @param options.transport_sender = Transport layer object with a method "send" that
	#	will be called after an envelope has been processed using the outbound chain.
	# @param options.transport_receiver = Transport layer object with methods "register"
	#	and "on_envelope_received", which are called when a new envelope registration
	#	is made on the Coordiantor and when the Coordinator needs to preprocess envelopes
	#	coming from the transport layer (respectively).
	# --------------------------------------------------------------------------------
	# @returns this object
	# --------------------------------------------------------------------------------
	constructor: (options = {}) ->
		ops = _.clone Coordinator.defaults
		_.extend ops, options
		@inbound_chain = ops.inbound_chain
		@outbound_chain = ops.outbound_chain
		@transport_sender = ops.transport_sender
		@transport_receiver = ops.transport_receiver
		@transport_receiver.on_envelope_received @__dispatch
	
	# Send an envelope through the bus.
	# --------------------------------------------------------------------------------
	# @param envelope Envelope to send across the bus
	# --------------------------------------------------------------------------------
	# @returns Nothing
	# --------------------------------------------------------------------------------
	send_envelope: (envelope) =>
		me = @
		first_call = (callback) -> callback(null, envelope, {})
			
		handler_chain = _.flatten([ first_call, @outbound_chain ])
		
		finish = (error, envelope, context) ->
			if error?
				message = "An error occurred in the outbound processing chain."
				exception = new ex.ProcessorChainException(message, error.error, error.processor, envelope)
				logger.error exception
			else
				me.transport_sender.send(envelope)
				
		try
			async.waterfall(handler_chain, finish)
		catch err
			message = "Uncaught exception occurred in the outbound processor chain"
			exception = new ex.SendException(message, err, envelope)
			logger.error exception
		
	# Register handlers and filters for a particular set of messages
	# --------------------------------------------------------------------------------
	# @param registration.info = Hash with the necessary registration information
	#		to allow the transport layer to route messages to the client.
	# @param registration.filter = Filters messages before they are sent to the 
	#		coordinator (before inbound processor chain)
	# @param registration.handle = Handle a successfully processed envelope
	# @param registration.fail = Handle an envelope that errored during
	#		the processing chain.  The transport layer will still be responsible 
	#		for handling implementation specific reliability stuff.
	# --------------------------------------------------------------------------------
	# @returns Nothing
	# --------------------------------------------------------------------------------
	register: (registration) =>
		me = @
		@transport_receiver.register registration
	
	# !!!INTERNAL!!! - Registered with the transport receiver, called when new envelopes
	# are received from the transport layer.
	# --------------------------------------------------------------------------------
	# @param dispatcher.envelope = Envelope received by the transport layer
	# @param dispatcher.dispatch = Called if the envelope has been processed
	#	successfully by this handler. Signature: (mutated_envelope)
	# @param dispatcher.fail = Called if the envelope was not successfully processed by
	#	the inbound chain.  Signature: (exception)
	# --------------------------------------------------------------------------------
	# @returns Nothing
	# --------------------------------------------------------------------------------
	__dispatch: (dispatcher) =>
		me = @
		first_call = (callback) -> callback(null, dispatcher.envelope, {})
		handler_chain = _.flatten([ first_call, me.inbound_chain ])
		
		finish = (error, envelope, context) ->
			# Caught exception
			if error?
				message = "An error occurred in the inbound processing chain."
				exception = new ex.ProcessorChainException(message, error.error, error.processor, dispatcher.envelope)
				logger.error exception
				dispatcher.fail(exception)
			else
				dispatcher.dispatch(envelope)
				
		try
			async.waterfall handler_chain, finish
		# Uncaught exception
		catch err
			message = "An uncaught exception occurred in the inbound processing chain."
			exception = new ex.DispatchException(message, err, dispatcher.envelope)
			logger.error exception
			dispatcher.fail(exception)
		

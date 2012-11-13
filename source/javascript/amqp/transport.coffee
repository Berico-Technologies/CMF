amqp = (require "amqp")
topo = (require "./topology")
LocalTopologyService = topo.LocalTopologyService

class EnvelopeDispatcher
	
	constructor: (@registration, @envelope) ->
		
	dispatch: (envelope) =>
		@registration.handle(envelope)
		
	fail: (exception) =>
		@registration.fail(exception)


class AmqpTransportLayer
	
	@defaults = 
		topology_service = new LocalTopologyService()
	
	constructor: (options) ->
		
		
	send: (envelope) =>
		
		
	on_envelope_received: (dispatcher) =>
		
		
	register: (registration) =>
		
		
	
	
	
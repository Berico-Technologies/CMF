amqp = require "amqp"
_ = require "underscore"

module.exports = (config) ->

	logger = config.logger ? require "winston"
	
	
	
	class AmqpDispatcher
		
		dispatch: (envelope) =>
			logger.debug "AmqpDispatcher.dispatch >> Dispatching envelope"
	
		dispatchFailed: (envelope, exception) =>
			logger.error("AmqpDispatcher.dispatchFailed >> Could not dispatch envelope because: #{exception}")
			
			
	class AmqpTransportProvider
		
		@acceptAllFilter = (e) -> true

		constructor: () ->
			@registrations = []
			@topology = config.topology
			@channelFactory = config.channelFactory
			logger.debug "AmqpTransportProvider.ctor >> InMemoryTransportProvider instantiated"

		register: (registration) =>
			logger.debug "AmqpTransportProvider.register >> Received registration"

		
		unregister: (registration) =>
			logger.debug "AmqpTransportProvider.unregister >> Received unregistration"

	
		send: (envelope) =>
			logger.debug "AmqpTransportProvider.send >> Sending envelope"
			routingInfo = @topology.getRoutingInfo(envelope.headers)
			routes = []
			_.map routingInfo, (routeInfo) ->
				routes.push routeInfo.producerRoute
			_.map routes, (route) ->
				channel = @channelFactory.getChannelFor route
		
		onEnvelopeReceived: (callback) =>
			logger.debug "AmqpTransportProvider.onEnvelopeReceived >> Registering callback handler"
		
					
	return new AmqpTransportProvider()

amqp = require "amqp"
_ = require "lodash"
logger = require "../logger"
HeaderConstants = (require "../envelope").HeaderConstants

class AmqpTransportProvider
	
	@acceptAllFilter = (e) -> true
	
	@routeToExchangeOptions = (route) ->
		opts =
			type: route.exchangeType
			durable: route.isDurable
			autoDelete: route.isAutoDelete
			confirm: true
		return opts
		
	@routeToPublishOptions = (route, envelope) ->
		opts =
			contentType: envelope.type() ? "application/json"
			headers: envelope.headers
		return opts

	constructor: (config) ->
		config = config ? {}
		@registrations = []
		@listeners = []
		@topology = config.topologyService
		@connectionFactory = config.connectionFactory
		@amqpListenerClass = config.amqpListenerClass
		logger.debug "AmqpTransportProvider.ctor >> instantiated"

	register: (registration) =>
		logger.debug "AmqpTransportProvider.register >> Received registration"
		routes = @_getRoutes registration.info, "consumerRoute"
		me = @
		logger.debug "AmqpTransportProvider.register >> Consuming from #{routes.length} routes"
		_.map routes, (route) ->
			me._initiateConnection route, true, (exchange, connection) ->
				listenerConfig =
					registration: registration
					route: route
					exchange: exchange
					connection: connection
					envelopeReceivedCallbacks: me.envelopeReceivedCallbacks
				listener = new @amqpListenerClass listenerConfig, ->
					logger.debug "AmqpTransportProvider.register >> Listener started"
	
	unregister: (registration) =>
		logger.debug "AmqpTransportProvider.unregister >> Received unregistration"
	
	
	send: (envelope, callback) =>
		logger.debug "AmqpTransportProvider.send >> Sending envelope"
		routes = @_getRoutes envelope.headers, "producerRoute"
		me = @
		logger.debug "AmqpTransportProvider.send >> Publishing to #{routes.length} routes"
		_.map routes, (route) ->
			me._initiateConnection route, false, (exchange, connection) ->
				publishOptions = AmqpTransportProvider.routeToPublishOptions route, envelope
				logger.debug "AmqpTransportProvider.send >> Sending to exchange '#{route.exchange}'"
				console.log callback
				exchange.publish route.routingKey, envelope.payload(), publishOptions, callback
				logger.debug "AmqpTransportProvider.send >> Published message"
	
	onEnvelopeReceived: (callback) =>
		logger.debug "AmqpTransportProvider.onEnvelopeReceived >> Registering callback handler"
		@envelopeReceivedCallbacks = @envelopeReceivedCallbacks ? []
		@envelopeReceivedCallbacks.push callback
		
	_getRoutes: (context, routeType) =>
		routingInfo = @topology.getRoutingInfo(context)
		routes = []
		_.map routingInfo, (routeInfo) ->
			routes.push routeInfo[routeType]
		return routes
	
	_initiateConnection: (route, dedicatedConnection, onReady) =>
		logger.debug "AmqpTransportProvider._initiateConnection >> initiating connection to #{@_prettyRoute route}"
		me = @
		@connectionFactory.getConnectionFor route, dedicatedConnection, (connection, usePassive) ->
			logger.debug "AmqpTransportProvider._initiateConnection >> Connection received"
			exchangeOptions = AmqpTransportProvider.routeToExchangeOptions(route)
			exchangeOptions.passive = usePassive
			connection.exchange route.exchange, exchangeOptions, (exchange) ->
				logger.debug "AmqpTransportProvider._initiateConnection >> Exchange ready"
				onReady.call(me, exchange, connection)
	
	_prettyRoute: (route) -> "amqp://#{route.host}:#{route.port}#{route.vhost}!#{route.exchange}"
	

module.exports = (config) -> return new AmqpTransportProvider(config)

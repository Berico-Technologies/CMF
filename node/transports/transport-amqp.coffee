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

	register: (registration, callback) =>
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
					me.registrations.push { registration: registration, listener: listener }
					callback() if callback?
	
	unregister: (registration, callback) =>
		logger.debug "AmqpTransportProvider.unregister >> unregistering"
		registration = _.find @registrations, (reg) -> reg.registration is registration
		if registration?
			registration.listener.close()
			@registrations = _.without @registrations, registration
			logger.debug "AmqpTransportProvider.unregister >> closed listener and removed registration"
		callback() if callback?
	
	send: (envelope, callback) =>
		logger.debug "AmqpTransportProvider.send >> Sending envelope"
		routes = @_getRoutes envelope.headers, "producerRoute"
		me = @
		logger.debug "AmqpTransportProvider.send >> Publishing to #{routes.length} routes"
		_.map routes, (route) ->
			me._initiateConnection route, false, (exchange, connection) ->
				publishOptions = AmqpTransportProvider.routeToPublishOptions route, envelope
				logger.debug "AmqpTransportProvider.send >> Sending to exchange '#{route.exchange}'"
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
	
	close: (callback) =>
		logger.info "AmqpTransportProvider.close >> closing all active listeners"
		_.map @registrations, (registration) ->
			registration.listener.close()
		@registrations.length = 0
		callback() if callback?

module.exports = (config) -> return new AmqpTransportProvider(config)

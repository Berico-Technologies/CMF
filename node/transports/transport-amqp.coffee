amqp = require "amqp"
_ = require "lodash"
logger = require "../logger"
HeaderConstants = (require "../envelope").HeaderConstants

class AmqpDispatcher
	
	dispatch: (envelope) =>
		logger.debug "AmqpDispatcher.dispatch >> Dispatching envelope"

	dispatchFailed: (envelope, exception) =>
		logger.error("AmqpDispatcher.dispatchFailed >> Could not dispatch envelope because: #{exception}")
		
class AmqpTransportProvider
	
	@acceptAllFilter = (e) -> true
	
	@routeToExchangeOptions = (route) ->
		opts =
			type: route.exchangeType
			durable: route.isDurable
			autoDelete: route.isAutoDelete
		return opts
		
	@routeToPublishOptions = (route, envelope) ->
		opts =
			contentType: envelope.type() ? "application/json"
			headers: envelope.headers
		return opts

	constructor: (config) ->
		config = config ? {}
		@registrations = []
		@topology = config.topologyService
		@connectionFactory = config.connectionFactory
		logger.debug "AmqpTransportProvider.ctor >> instantiated"

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
		me = @
		logger.debug "AmqpTransportProvider.send >> Publishing to #{routes.length} routes"
		_.map routes, (route) ->
			logger.debug "AmqpTransportProvider.send >> Sending to exchange '#{route.exchange}'"
			me.connectionFactory.getConnectionFor route, (connection) ->
				logger.debug "AmqpTransportProvider.send >> Connection received"
				exchangeOptions = AmqpTransportProvider.routeToExchangeOptions(route)
				connection.exchange route.exchange, exchangeOptions, (exchange) ->
					logger.debug "AmqpTransportProvider.send >> Exchange ready"
					publishOptions = AmqpTransportProvider.routeToPublishOptions route, envelope
					exchange.publish route.routingKey, envelope.payload(), publishOptions
					logger.debug "AmqpTransportProvider.send >> Published message"
	
	onEnvelopeReceived: (callback) =>
		logger.debug "AmqpTransportProvider.onEnvelopeReceived >> Registering callback handler"

module.exports = (config) -> return new AmqpTransportProvider(config)

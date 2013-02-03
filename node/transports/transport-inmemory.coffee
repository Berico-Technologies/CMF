_ = require "lodash"
logger = require "../logger"

class InMemoryTransportProvider
	
	@acceptAllFilter = (e) -> true
	@routeByType = (info, envelope) ->
		return info.type is envelope.type()

	constructor: (config) ->
		config = config ? {}
		@registrations = []
		@isRouteMatch = config.routingPredicate ? InMemoryTransportProvider.routeByType
		logger.debug "InMemoryTransportProvider.ctor >> InMemoryTransportProvider instantiated"

	register: (registration) =>
		logger.debug "InMemoryTransportProvider.register >> Received registration"
		registration.filter = InMemoryTransportProvider.acceptAllFilter unless registration.filter?
		@registrations.push registration
	
	unregister: (registration) =>
		logger.debug "InMemoryTransportProvider.unregister >> Received unregistration"
		@registration = _.without @registration, registration

	send: (envelope) =>
		logger.debug "InMemoryTransportProvider.send >> Sending envelope"
		dispatcher = @
		_.map @envelopeReceivedCallbacks, (callback) ->
			callback(envelope, dispatcher)
	
	onEnvelopeReceived: (callback) =>
		logger.debug "InMemoryTransportProvider.onEnvelopeReceived >> Registering callback handler"
		@envelopeReceivedCallbacks = @envelopeReceivedCallbacks ? []
		@envelopeReceivedCallbacks.push callback

	dispatch: (envelope) =>
		logger.debug "InMemoryTransportProvider.dispatch >> Dispatching envelope"
		me = @
		_.map @registrations, (registration) ->
			if registration.filter envelope and me.isRouteMatch registration.info, envelope
				# if the handle method fails, the Envelope Bus will call dispatchFailed
				registration.handle envelope

	dispatchFailed: (envelope, exception) =>
		logger.error("InMemoryTransportProvider.dispatchFailed >> Could not dispatch envelope because: #{exception}")
		me = @
		_.map @registrations, (registration) ->
			try
				if registration.filter envelope and me.isRouteMatch registration.info, envelope
					registration.handleFailed envelope, exception if registration.handleFailed?
			catch ex
				logger.error("InMemoryTransportProvider.dispatchFailed >> Error trying to call fail handler for envelope: #{ex}")

module.exports = (config) -> return new InMemoryTransportProvider(config)
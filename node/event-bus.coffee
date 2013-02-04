_ = require "lodash"
logger = require "./logger"
Envelope = require "./envelope"
jsonProcessor = require "./processors/event/serializer-json"
outboundRoutingProcessor = require "./processors/event/outbound-routing"

class EventRegistration
	
	constructor: (@eb, registrationContext) ->
		@info = registrationContext
		@filter = registrationContext.filter ? (e) -> true
		@handleCallback = registrationContext.handle
		@handleFailedCallback = registrationContext.handleFailed ? (env, e) -> null
		logger.debug "EventRegistration.ctor >> instantiated"
	
	handle: (envelope) =>
		logger.debug "EventRegistration.handle >> handling envelope"
		result = @eb._processInbound envelope
		if @filter result.event
			@handleCallback result.event, result.headers
	
	handleFailed: (envelope, ex) =>
		logger.debug "EventRegistration.handleFailed >> handling failed reception"
		@handleFailedCallback envelope, ex

class EventBus
	
	@DefaultInboundProcessors = [ 
		jsonProcessor.inbound 
	]
	
	@DefaultOutboundProcessors = [ 
		outboundRoutingProcessor
		jsonProcessor.outbound
	]
	
	constructor: (config) ->
		@inboundProcessors = config.inboundEventProcessors ? EventBus.DefaultInboundProcessors
		@outboundProcessors = config.outboundEventProcessors ? EventBus.DefaultOutboundProcessors
		@envelopeBus = config.envelopeBus
		@subscriptions = []
		logger.debug "EventBus.ctor >> Event Bus started"
	
	publish: (event, context, callback) =>
		logger.debug "EventBus.publish >> Sending event."
		callback = context if _.isFunction context
		context = {} if _.isFunction context
		results = @_processOutbound event, context
		if results.wasSuccessful
			@envelopeBus.send results.envelope, callback
	
	subscribe: (handlingContext, callback) =>
		logger.debug "EventBus.subscribe >> subscribing to event"
		registrationContext = _.extend { logger: logger }, handlingContext
		registration = new EventRegistration @, registrationContext
		@subscriptions.push { context: handlingContext, registration: registration }
		@envelopeBus.register registration, callback
		
	unsubscribe: (handlingContext, callback) =>
		logger.debug "EventBus.unsubscribe >> unsubscribing from event"
		subscription = _.find @subscriptions, (sub) -> sub.context is handlingContext
		@subscriptions = _.without @subscriptions, subscription
		@envelopeBus.unregister subscription.registration, callback
	
	close: (callback) =>
		logger.debug "EventBus.close >> closing the EventBus"
		@envelopeBus.close(callback)
	
	_processInbound: (envelope) =>
		logger.debug "EventBus._processOutbound >> Processing inbound message"
		eventObject = {}
		wasSuccessful = @_processEvent @inboundProcessors, envelope, eventObject, {}
		results =
			event: eventObject
			headers: envelope.headers
			wasSuccessful: wasSuccessful
		return results
	
	_processOutbound: (event, context) =>
		logger.debug "EventBus._processOutbound >> Processing outbound message"
		envelope = new Envelope()
		wasSuccessful = @_processEvent @outboundProcessors, envelope, event, context
		results =
			envelope: envelope
			wasSuccessful: wasSuccessful
		return results
		
	_processEvent: (chain, envelope, eventObject, context) =>
		logger.debug "EventBus._processEvent >> Processing event"
		context = { logger: logger }
		wasSuccessful = true
		_.map chain, (processor) ->
			try
				result = processor(envelope, eventObject, context) ? true
				if result is false
					wasSuccessful = false
			catch ex
				logger.error "EventBus._processEvent >> Processor failed to handle event: #{ex}"
				wasSuccessful = false
		return wasSuccessful
		
module.exports = (config) -> new EventBus(config)
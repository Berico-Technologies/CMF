_ = require "lodash"
logger = require "./logger"
Envelope = require "./envelope"

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
		(envelope, event, context) ->
			context.logger.debug "DefaultInboundProcessor1 >> parsing payload"
			if envelope.type() is "application/json"
				event = JSON.parse(envelope.payload())
	]
	
	@DefaultOutboundProcessors = [
		(envelope, event, context) ->
			context.logger.debug "DefaultOutboundProcessor1 >> packaging for transport"
			envelope.payload(JSON.stringify event)
			envelope.type("application/json")
			topic = event.topic ? context.topic
			envelope.topic(topic)
	]
	
	constructor: (config) ->
		@inboundProcessors = config.inboundEventProcessors ? EventBus.DefaultInboundProcessors
		@outboundProcessors = config.outboundEventProcessors ? EventBus.DefaultOutboundProcessors
		@envelopeBus = config.envelopeBus
		logger.debug "EventBus.ctor >> Event Bus started"
	
	publish: (event, context) =>
		logger.debug "EventBus.publish >> Sending event with context: #{context}"
		context = context ? {}
		results = @_processOutbound event, context
		if results.wasSuccessful
			@envelopeBus.send results.envelope
	
	subscribe: (handlingContext) =>
		logger.debug "EventBus.subscribe >> subscribing to event"
		registrationContext = _.extend { logger: logger }, handlingContext
		registration = new EventRegistration @, registrationContext
		@envelopeBus.register registration
		
	_processInbound: (envelope) =>
		logger.debug "EventBus._processOutbound >> Processing inbound message"
		event = {}
		wasSuccessful = @_processEvent @inboundProcessors, envelope, event, {}
		results =
			event: event
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
		
	_processEvent: (chain, envelope, event, context) =>
		logger.debug "EventBus._processEvent >> Processing event"
		context = { logger: logger }
		wasSuccessful = true
		_.map chain, (processor) ->
			try
				result = processor(envelope, event, context) ? true
				if result is false
					wasSuccessful = false
			catch ex
				logger.error "EventBus._processEvent >> Processor failed to handle event: #{ex}"
				wasSuccessful = false

		return wasSuccessful
		
module.exports = (config) -> new EventBus(config)
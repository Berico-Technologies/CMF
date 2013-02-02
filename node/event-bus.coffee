_ = require "underscore"
config = require "./config"
logger = config.logger ? require "winston"
Envelope = require "./envelope"
EnvelopeBus = require "./envelope-bus"

class EventRegistration
	
	constructor: (@eb, conf) ->
		@info = conf
		@filter = conf.filter ? (e) -> true
		@handleCallback = conf.handle
		@handleFailedCallback = conf.handleFailed ? (env, e) -> null
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
			logger.debug "DefaultInboundProcessor1 >> parsing payload"
			if envelope.type() is "application/json"
				event = JSON.parse(envelope.payload())
	]
	
	@DefaultOutboundProcessors = [
		(envelope, event, context) ->
			logger.debug "DefaultOutboundProcessor1 >> packaging for transport"
			envelope.payload(JSON.stringify event)
			envelope.type("application/json")
			topic = event.topic ? context.topic
			envelope.topic(topic)
	]
	
	constructor: (conf) ->
		conf = conf ? config
		@inboundProcessors = conf.inboundEventProcessors ? EventBus.DefaultInboundProcessors
		@outboundProcessors = conf.outboundEventProcessors ? EventBus.DefaultOutboundProcessors
		@envelopeBus = conf.envelopeBus ? new EnvelopeBus(conf)
		logger.debug "EventBus.ctor >> Event Bus started"
	
	publish: (event, context) =>
		logger.debug "EventBus.publish >> Sending event with context: #{context}"
		context = context ? {}
		results = @_processOutbound event, context
		if results.wasSuccessful
			@envelopeBus.send results.envelope
	
	subscribe: (handlingContext) =>
		logger.debug "EventBus.subscribe >> subscribing to event"
		registration = new EventRegistration @, handlingContext
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
		context = {}
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
		
module.exports = EventBus
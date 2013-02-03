_ = require "lodash"
logger = require "./logger"

# Receives and dispatches Envelopes with the supplied 
# transport provider
class EnvelopeBus
	
	constructor: (config) ->
		@inboundProcessors = config.inboundProcessors ? []
		@outboundProcessors = config.outboundProcessors ? []
		@transportProvider = config.transportProvider
		@transportProvider.onEnvelopeReceived @_handleIncomingEnvelope
		logger.debug "EnvelopeBus.ctor >> EnvelopeBus instantiated"
	
	send: (envelope, callback) =>
		logger.debug "EnvelopeBus.send >> Sending envelope #{envelope}"
		if _.isUndefined envelope or _.isNull envelope
			throw "Envelope must not be null" 
		isValid = @_processOutbound envelope
		logger.debug "EnvelopeBus.send >> Outcome of outbound chain: #{isValid}"
		@transportProvider.send envelope, callback if isValid
	
	register: (registration) =>
		logger.debug "EnvelopeBus.register >> registering handler"
		if _.isUndefined registration or _.isNull registration
			throw "Registration must not be null" 
		@transportProvider.register registration
		
	unregister: (registration) =>
		logger.debug "EnvelopeBus.unregister >> unregistering handler"
		if _.isUndefined registration or _.isNull registration
			throw "Registration must not be null"
		@transportProvider.unregister registration
	
	_processInbound: (envelope) =>
		logger.debug "EnvelopeBus._processInbound >> processing inbound envelope"
		return @_processEnvelope @inboundProcessors, envelope
	
	_processOutbound: (envelope) =>
		logger.debug "EnvelopeBus._processOutbound >> processing outbound envelope"
		return @_processEnvelope @outboundProcessors, envelope
	
	_processEnvelope: (chain, envelope) =>
		logger.debug "EnvelopeBus._processEnvelope >> processing envelope"
		context = {}
		wasSuccessful = true
		me = @
		_.map chain, (processor) ->
			try
				result = processor(envelope, context) ? true
				if result is false
					wasSuccessful = false
			catch ex
				me.logger.error "EnvelopeBus._processEnvelope >> Processor failed to handle envelope: #{ex}"
				wasSuccessful = false
		
		return wasSuccessful
	
	_handleIncomingEnvelope: (envelope, dispatcher) =>
		logger.debug "EnvelopeBus._handleIncomingEnvelope >> handling envelope inbound from transport layer"
		try
			isValid = @_processInbound envelope
			logger.debug "EnvelopeBus._handleIncomingEnvelope >> Outcome of inbound chain: #{isValid}"
			if isValid
				dispatcher.dispatch envelope
				logger.debug "EnvelopeBus._handleIncomingEnvelope >> Envelope Dispatched"
		catch ex
			logger.warn "EnvelopeBus._handleIncomingEnvelope >> Failed to dispatch envelope"
			dispatcher.dispatchFailed envelope, ex
		
	
module.exports = (config) -> return new EnvelopeBus(config)


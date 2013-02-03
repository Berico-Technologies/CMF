_ = require "lodash"
logger = require "../../logger"
Envelope = (require "../../envelope")

class AmqpListener
	
	@routeToQueueOptions = (route) ->
		opts =
			durable: route.isDurable
			autoDelete: route.isAutoDelete
		return opts
	
	constructor: (config, onReady) ->
		@registration = config.registration
		@route = config.route
		@exchange = config.exchange
		@connection = config.connection
		@envelopeReceivedCallbacks = config.envelopeReceivedCallbacks
		@messageBuffer = {}
		logger.debug  "AmqpListener.ctor >> creating binding"
		@_bind onReady
		logger.debug "AmqpListener.ctor >> instantiated"
		
	_bind: (onReady) =>
		logger.debug "AmqpListener._bind >> binding to connection"
		me = @
		queueOptions = AmqpListener.routeToQueueOptions @route
		@connection.queue @route.queue, queueOptions, (queue) ->
			logger.debug "AmqpListener._bind >> bind to queue '#{queue.name}'"
			me.queue = queue
			logger.debug "AmqpListener._bind >> subscribing to raw AMQP messages"
			me.queue.subscribeRaw { ack: true }, me._receiveEnvelope
			logger.debug "AmqpListener._bind >> binding queue to #{me.exchange} using #{me.route.routingKey}"
			me.queue.bind me.exchange, me.route.routingKey
			onReady.call(me)
	
	_receiveEnvelope: (message) =>
		me = @
		logger.debug "AmqpListener._receiveEnvelope >> Message Received"
		headers = @_getHeaders message
		logger.debug "AmqpListener._receiveEnvelope >> HEADERS", headers
		@_getMessageBody message, (buffer) ->
			logger.debug "AmqpListener._receiveEnvelope >> Received Message Body"
			envelope = new Envelope(headers, buffer)
			# We will save a reference to the message so we can 
			# acknowledge or reject it later.
			me.messageBuffer[envelope.id()] = message
			logger.debug "AmqpListener._receiveEnvelope >> Notifying EnvelopeReceivedCallbacks"
			console.log me.envelopeReceivedCallbacks
			# Notify all of the listeners
			_.map me.envelopeReceivedCallbacks, (callback) ->
				callback(envelope, me)
	
	_getMessageBody: (message, onFinish) =>
		me = @
		buffer = new Buffer message.size
		buffer.used = 0
		message.addListener "data", (data) ->
			data.copy(buffer, buffer.used)
			buffer.used += data.length
		message.addListener "end", ->
			onFinish.call(me, buffer)
	
	_getHeaders: (message) =>
		headers = {}
		for key, value of message.headers
			headers[key] = value
		return headers
	
	dispatch: (envelope) =>
		logger.debug "AmqpListener.dispatch >> Dispatching envelope"
		message = @messageBuffer[envelope.id()]
		result = true
		if @registration.filter envelope
			result = @registration.handle envelope
		if result is false then message.reject() else message.acknowledge()

	dispatchFailed: (envelope, exception) =>
		logger.error("AmqpListener.dispatchFailed >> Could not dispatch envelope because: #{exception}")
		message = @messageBuffer[envelope.id()]
		try
			result = false
			result = @registration.handleFailed envelope
			if result is true then message.acknowledge() else message.reject()
		catch ex
			logger.error "AmqpListener.dispatchFailed >> Registration.handleFailed threw exception: #{ex}"
			message.reject()
	
module.exports = AmqpListener
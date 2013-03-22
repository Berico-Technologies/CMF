logger = require "../logger"
eventBus = (require "../cmf").amqp.eventBus()

subscription = 
	topic: "test-topic"
	handle: (event, headers) ->
		logger.debug "Event: #{event.msg}"
	handleFailed: (envelope, exception) ->
		logger.error "Failed to handle event #{exception}"

eventBus.subscribe subscription

stopConsuming = ->
	eventBus.close()
			
setTimeout stopConsuming, 10000
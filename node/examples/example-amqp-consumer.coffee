logger = require "../logger"

eventBus = (require "../event-bus")({
	envelopeBus: (require "../envelope-bus")({
		transportProvider: (require "../transports/transport-amqp")({
			connectionFactory: (require "../transports/amqp/connection-factory")()
			topologyService: (require "../transports/amqp/topology-simple")()
			amqpListenerClass: require "../transports/amqp/listener"
		})
	})
})

eventBus.subscribe {
	topic: "test-topic"
	handle: (event, headers) ->
		logger.debug "Event: #{event.msg}"
	handleFailed: (envelope, exception) ->
		logger.error "Failed to handle event #{exception}"
}
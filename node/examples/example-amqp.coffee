config = 
	envelopeBus: (require "../envelope-bus")({
		transportProvider: (require "../transports/transport-amqp")({
			connectionFactory: (require "../transports/amqp/connection-factory")()
			topologyService: (require "../transports/amqp/topology-simple")()
			amqpListenerClass: require "../transports/amqp/listener"
		})
	})

eventBus = (require "../event-bus")(config)

eventBus.subscribe {
	topic: "test-topic"
	handle: (event, headers) ->
		console.log "Got event: #{event}"
	handleFailed: (envelope, exception) ->
		console.log "Failed to handle event #{exception}"
}

fireMessage = () ->
	eventBus.publish { msg: "hi mom!", topic: "test-topic" }

setInterval fireMessage, 1000
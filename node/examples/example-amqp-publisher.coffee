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

recursivePublish = () ->
	eventBus.publish { msg: "hi mom!", topic: "test-topic" }, recursivePublish

recursivePublish()
config = 
	envelopeBus: (require "../envelope-bus")({
		transportProvider: (require "../transports/transport-amqp")({
			connectionFactory: (require "../transports/amqp/connection-factory")()
			topologyService: (require "../transports/amqp/topology-simple")()
		})
	})

eventBus = (require "../event-bus")(config)

eventBus.publish { msg: "hi mom!", topic: "test-topic" }
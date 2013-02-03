###
I'm going to clean up the config section once I get the AMQP transport working.
###
module.exports = 
	transportProvider: (require "./transports/transport-inmemory")()
	amqpTransportProvider: (require "./transports/transport-amqp")({
		connectionFactory: require "./transports/amqp/connection-factory"
	})
	inboundProcessors: [ 
		(envelope, context) -> 
			envelope.headers.newKey = "Header Was Set"
	]
	outboundProcessors: []
	
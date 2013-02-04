_ = require "lodash"

defaults =
	eventBus: "./event-bus"
	envelopeBus: "./envelope-bus"

amqpDefaults =
	transportProvider: "./transports/transport-amqp"
	connectionFactory: "./transports/amqp/connection-factory"
	topologyService: "./transports/amqp/topology-simple"
	amqpListenerClass: "./transports/amqp/listener"

inmemDefaults =
	transportProvider: "./transports/transport-inmemory"

# A Simple Factory for configuring an instantiating an 
# AMQP-backed Event or Envelope Bus
class AmqpBusFactory
	
	# The AMQP Transport is a little more complex, so we've
	# created a function to configure it.
	@_transport: (config) ->
		console.log config
		conf = 
			connectionFactory: (require amqpDefaults.connectionFactory)(config)
			topologyService: (require amqpDefaults.topologyService)(config)
			amqpListenerClass: require amqpDefaults.amqpListenerClass
		_.extend conf, config
		return (require amqpDefaults.transportProvider)(conf)
	
	# Get an preconfigured AMQP Event Bus instance
	@eventBus: (config) ->
		envelopeBus = AmqpBusFactory.envelopeBus config
		conf = 
			envelopeBus: envelopeBus
		_.extend conf, config
		return (require defaults.eventBus)(conf)
	
	# Get an preconfigured AMQP Envelope Bus instance
	@envelopeBus: (config) ->
		conf = 
			transportProvider: AmqpBusFactory._transport(config)
		_.extend conf, config
		return (require defaults.envelopeBus)(conf)

# A Simple Factory for configuring an instantiating an 
# In-Memory Event or Envelope Bus
class InMemoryBusFactory
	
	# Get an preconfigured In-Memory Event Bus instance
	@eventBus: (config) ->
		envelopeBus = InMemoryBusFactory.envelopeBus config
		conf = 
			envelopeBus: envelopeBus
		_.extend conf, config
		return (require defaults.eventBus)(conf)
		
	# Get an preconfigured In-Memory Envelope Bus instance
	@envelopeBus: (config) ->
		conf =
			transportProvider: (require inmemDefaults.transportProvider)(config)
		_.extend conf, config
		return (require defaults.envelopeBus)(conf)

module.exports = 
	amqp: AmqpBusFactory
	inmem: InMemoryBusFactory

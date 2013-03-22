Envelope = require "../envelope"

config = 
	transportProvider: (require "../transports/transport-inmemory")()
	inboundProcessors: [ 
		(envelope, context) -> 
			envelope.headers.newKey = "Header Was Set"
	]

# Instantiate the Envelope Bus
envelopeBus = (require "../envelope-bus")(config)

envelope = new Envelope().topic("test").type("json").payload({ message: "blah" })

envelopeBus.register {
	info: { topic: "test", type: "json" }
	handle: (e) -> 
		console.log "Got envelope: #{e.headers.newKey}"
	handleFailed: (e, ex) -> console.log "Epic fail mate: #{ex}"
}

envelopeBus.send(envelope)

# Add the Envelope Bus to the Config
config.envelopeBus = envelopeBus

# Instantiate the Event Bus
eventBus = (require "../event-bus")(config)

eventBus.subscribe {
	topic: "mom channel"
	handle: (event, headers) ->
		console.log "Got event: #{event}"
	handleFailed: (envelope, exception) ->
		console.log "Failed to handle event #{exception}"
}

eventBus.publish { msg: "hi mom!", topic: "mom channel" }


EnvelopeBus = require "./envelope-bus"
EventBus = require "./event-bus"
Envelope = require "./envelope"



env = new Envelope().topic("test").type("json").payload({ message: "blah" })

eb = new EnvelopeBus()

eb.register {
	info: { topic: "test", type: "json" }
	handle: (e) -> 
		console.log "Got envelope: #{e.headers.newKey}"
	handleFailed: (e, ex) -> console.log "Epic fail mate: #{ex}"
}

eb.send(env)

eventBus = new EventBus()

eventBus.subscribe {
	topic: "mom channel"
	handle: (event, headers) ->
		console.log "Got event: #{event}"
	handleFailed: (envelope, exception) ->
		console.log "Failed to handle event #{exception}"
}

eventBus.publish { msg: "hi mom!", topic: "mom channel" }


assert = require "assert"

Envelope = (require "../envelope").Envelope
Coordinator = (require "../coordinator").Coordinator

conf = (require "../configuration")
logger = new ((require "winston").Logger)(conf.LogConf);


class EnvelopeDispatcher
	
	constructor: (@registration, @envelope) ->
		
	dispatch: (envelope) =>
		@registration.handle(envelope)
		
	fail: (exception) =>
		@registration.fail(exception)

class MockTransportLayer
	
	preprocessors: []
		
	register: (registration) =>
		@registration = registration
		
	on_envelope_received: (preprocessor)=>
		@preprocessors.push preprocessor
	
	test_envelope_received: (envelope) =>
		if @registration.filter(envelope)
			console.log "Dispatching"
			dispatcher = new EnvelopeDispatcher(@registration, envelope)
			for preprocessor in @preprocessors
				console.log "Handing to preprocessor"
				preprocessor(dispatcher)
				
# Instantiate the Transport Layer
mock_transport_layer = new MockTransportLayer()

# Create the options for the Coordinator instance
options = 
	inbound_chain: [ 
		(envelope, context, callback) -> 
			console.log "First handler in chain"
			envelope.headers.fn1 = "foo"
			envelope.payload += "foo"
			callback(null, envelope, context)
		(envelope, context, callback) -> 
			console.log "Second handler in chain"
			envelope.headers.fn2 = "bar"
			envelope.payload += "bar"
			callback(null, envelope, context)
	]
	transport_receiver: mock_transport_layer

# Instantiate the Coordinator
coordinator = new Coordinator(options)

# This is the expected envelope
expected = 
	headers: { fn1: "foo", fn2: "bar" }
	payload: "foobar"

# Construct a registration
registration = 
	info: {}
	filter: (envelope) -> true
	# We will make the assertions against the returned envelope;
	# i.e. how the envelope's state was mutated by the chain.
	handle: (actual) -> 
		console.log "Making assertions"
		assert.deepEqual expected.headers, actual.headers
		assert.equal expected.payload, actual.payload
	fail: (exception) -> null

# Register for Envelopes
coordinator.register registration

# Simulate an envelope being received by the transport layer
mock_transport_layer.test_envelope_received new Envelope({}, "")


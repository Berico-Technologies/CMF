assert      = (require "assert")
Envelope    = (require "../envelope").Envelope
Coordinator = (require "../coordinator").Coordinator
conf        = (require "../configuration")
logger      = new ((require "winston").Logger)(conf.LogConf);

class MockTransportLayer
	
	constructor: (@expected) ->
		
	send: (actual) =>
		assert.deepEqual @expected.headers, actual.headers
		assert.equal @expected.payload, actual.payload
		
# This is the envelope we expect after the envelope we are
# sending runs through the outbound handler chain
expected_envelope = 
	headers: { fn1: "foo", fn2: "bar" }
	payload: "foobar"

# Instantiate the outbound chain handler
mock_transport_layer = new MockTransportLayer(expected_envelope)

# Setup the options for the Coordinator
options = 
	outbound_chain: [ 
		(envelope, context, callback) -> 
			envelope.headers.fn1 = "foo"
			envelope.payload += "foo"
			callback(null, envelope, context)
		(envelope, context, callback) -> 
			envelope.headers.fn2 = "bar"
			envelope.payload += "bar"
			callback(null, envelope, context)
	]
	transport_sender: mock_transport_layer

# Instantiate the Coordinator
coordinator = new Coordinator(options)

# Send the Envelope (the assertions will be made after)
coordinator.send_envelope new Envelope({}, "")


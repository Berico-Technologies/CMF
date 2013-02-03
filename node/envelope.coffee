Afluent = require "./afluent"
logger = require "./logger"

# These represent the header values we use in CMF for
# routing, security, serialization, etc.  This list is not
# definitive, and you may need to add a few for your
# messaging purposes.
Constants =
	ENVELOPE_CREATION_TIME: 
		key: "cmf.bus.envelope.creation"
		property: "created"
	ENVELOPE_RECEIPT_TIME: 
		key: "cmf.bus.envelope.receipt"
		property: "received"
	MESSAGE_CORRELATION_ID: 
		key: "cmf.bus.message.correlation_id"
		property: "correlationId"
	MESSAGE_ID: 
		key: "cmf.bus.message.id"
		property: "id"
	MESSAGE_PATTERN: 
		key: "cmf.bus.message.pattern"
	MESSAGE_PATTERN_PUBSUB: 
		key: "cmf.bus.message.pattern#pub_sub"
	MESSAGE_PATTERN_RPC: 
		key: "cmf.bus.message.pattern#rpc"
	MESSAGE_PATTERN_RPC_TIMEOUT: 
		key: "cmf.bus.message.pattern#rpc.timeout"
	MESSAGE_SENDER_IDENTITY: 
		key: "cmf.bus.message.sender_identity"
		property: "sender"
	MESSAGE_SENDER_SIGNATURE: 
		key: "cmf.bus.message.sender_signature"
		property: "signature"
	MESSAGE_TOPIC: 
		key: "cmf.bus.message.topic"
		property: "topic"
	MESSAGE_TYPE: 
		key: "cmf.bus.message.type"
		property: "type"

# Envelope is the construct passed between clients and
# the broker.
class Envelope extends Afluent
	
	@HeaderConstants = Constants
	
	constructor: (headers, body) ->
		@headers = headers ? {}
		@body = body ? {}
		super("headers", Constants)
		logger.debug "Envelope.ctor >> created"
	
	# The payload of the message.
	payload: (value) =>
		if value?
			@body = value
			return @
		return @body

module.exports = Envelope
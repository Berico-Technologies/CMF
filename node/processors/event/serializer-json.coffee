_ = require "lodash"

class JsonEventProcessor
	
	inbound: (envelope, event, context)=>
		context.logger.debug "JsonEventProcessor.inbound >> Visited"
		if envelope.type() is "application/json" or envelope.type() is "text/json"
			context.logger.debug "JsonEventProcessor.inbound >> Deserializing JSON"
			strPayload = envelope.payload().toString()
			
			# It's import to extend because we are relying on the object reference
			# to not change.
			_.extend event, JSON.parse(strPayload)
	
	outbound: (envelope, event, context) =>
		context.logger.debug "JsonEventProcessor.outbound >> Serializing to JSON"
		json = JSON.stringify(event)
		envelope.payload(json)
		envelope.type("application/json")
		
module.exports = new JsonEventProcessor()
_ = require "lodash"

module.exports = (envelope, event, context) ->
	topic = event.topic ? context.topic
	if _.isUndefined topic
		context.logger.warn "Outbound-Routing-Event-Processor >> 'topic' must be defined either on the event or in the context."
		return false
	envelope.topic(topic)
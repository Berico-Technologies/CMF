
x = module.exports ? this

class x.ProcessorChainException
	constructor: (@message, @error, @failing_processor, @envelope = null) ->

class x.DispatchException
	constructor: (@message, @error, @envelope = null) ->

class x.SendException
	constructor: (@message, @error, @envelope = null) ->
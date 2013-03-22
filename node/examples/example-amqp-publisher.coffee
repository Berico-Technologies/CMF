logger = require "../logger"
eventBus = (require "../cmf").amqp.eventBus({host: "127.0.0.1"})

recursivePublish = ->
	eventBus.publish { msg: "hi mom!", topic: "test-topic" }, recursivePublish

recursivePublish()

andWereDone = ->
	eventBus.close -> process.exit()
	
setTimeout andWereDone, 10000
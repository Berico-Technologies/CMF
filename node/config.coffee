winston = require "winston"
dfmt = require "dateformat"
now  = new Date()

# Winston Logger config
logger = new (winston.Logger)({
	transports: [
		new (winston.transports.Console)({ 
			level: "debug" 
			colorize: true
			timestamp: true
		}),
		new (winston.transports.File)({
			filename: "./logs/cmf-log-#{ dfmt(now, 'yyyy.mm.dd.HH') }.txt",
			handleExceptions: true
		})
	]
})

# Transport Provider
imtp = (require "./transports/transport-inmemory")({ logger: logger })

module.exports = 
	logger: logger
	transportProvider: imtp
	inboundProcessors: [ 
		(envelope, context) -> 
			envelope.headers.newKey = "Header Was Set"
	]
	outboundProcessors: []
winston = require "winston"
dfmt = require "dateformat"
now  = new Date()

module.exports = new (winston.Logger)({
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
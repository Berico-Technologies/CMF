winston = require "winston"

x = module.exports

x.LogConf = 
	transports: [
		new (winston.transports.Console)(),
		new (winston.transports.File)({ filename: 'cmf.bus.core.log' })
	]
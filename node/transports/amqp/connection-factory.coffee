amqp = require "amqp"
logger = require "../../logger"

class ConnectionFactory

	@DefaultConnectionStrategy = (route) ->
		return "amqp://#{route.host}:#{route.port}#{route.vhost}!#{route.exchange}"

	constructor: (config) ->
		config = config ? {}
		@username = config.username ? "guest"
		@password = config.password ? "guest"
		@connectionPool = {}
		@connectionStrategy = config.connectionStrategy ? ConnectionFactory.DefaultConnectionStrategy
		logger.debug "ConnectionFactory.ctor >> instantiated."

	getConnectionFor: (route, dedicated, callback) ->
		logger.debug "ConnectionFactory.getConnectionFor >> Getting route"
		connectionName = @connectionStrategy(route)
		connection = @connectionPool[connectionName]
		if dedicated or not connection?
			logger.debug "ConnectionFactory.getConnectionFor >> creating new connection"
			connection = @_createConnection route
			@connectionPool[connectionName] = connection unless dedicated
			connection.on("ready", () -> callback(connection, false))
		else
			logger.debug "ConnectionFactory.getConnectionFor >> returning existing connection"
			callback(connection, true)
			
	removeConnection: (route) ->
		logger.debug "ConnectionFactory.removeConnection >> Removing connection"
		connectionName = @connectionStrategy(route)
		connection = @connectionPool[connectionName]
		connection.end() if connection?
		delete @connectionPool[connectionName]

	_createConnection: (route) ->
		logger.debug "ConnectionFactory._createConnection >> Creating new connection"
		connection = amqp.createConnection({
			host: route.host, 
			port: route.port,
			login: @username,
			password: @password,
			vhost: route.vhost
		})
		return connection

module.exports = (config) -> return new ConnectionFactory(config)
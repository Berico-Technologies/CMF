amqp = require "amqp"
config = "../../config"

class BasicConnectionFactory
	
	@DefaultConnectionStrategy = (route) ->
		return "#{route.host}|#{route.vhost}|#{route.exchange}"
	
	constructor: ->
		@username = config.username ? "guest"
		@password = config.password ? "guest"
		@connectionPool = {}
		@connectionStrategy = config.connectionStrategy ? BasicConnectionFactory.DefaultConnectionStrategy
	
	getConnectionFor: (route, callback) ->
		connectionName = @connectionStrategy(route)
		connection = @connectionPool[connectionName]
		if connection?
			callback(connection)
		else
			connection = @_createConnection route
			@connectionPool[connectionName] = connection
			connection.on("ready", callback)
	
	removeConnection: (route) ->
		connectionName = @connectionStrategy(route)
		connection = @connectionPool[connectionName]
		connection.end() if connection?
		delete @connectionPool[connectionName]
	
	_createConnection: (route) ->
		connection = amqp.createConnection({
			host: route.host, 
			port: route.port,
			login: @username,
			password: @password,
			vhost: route.vhost
		})
		return connection
		
module.exports = BasicConnectionFactory
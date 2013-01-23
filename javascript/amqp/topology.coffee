uuid = (require 'node-uuid')

x = module.exports

class x.Exchange
	
	constructor: (options) ->
		@name = options.name ? ""
		@host = options.host
		@port = options.port ? 5672
		@vhost = options.vhost ? "/"
		@exchange_type = options.exchange_type

class x.BindingInfo
	
	constructor: (options) ->
		@routing_key = options.routing_key ? '#'
		@queue_name = options.queue_name ? uuid.v4()
		@is_durable = options.is_durable ? false
		@is_auto_delete = options.is_auto_delete ? true

class x.ProducerRoute extends x.Route
	
	constructor: (@context, @exchange) ->

class x.ConsumerRoute extends x.ProducerRoute
	
	constructor: (context, exchange, @bindinginfo) ->
		super context, exchange

class x.RoutingInfo

	constructor: (@routes) ->
	
	get_matches: (is_match) =>
		matches = []
		for route in @routes
			if is_match route
				matches.push route
		return matches


class x.LocalTopologyService
	
	constructor: () ->
		
	get_producing_routes: (routingHints) ->
		
		
		
	get_consuming_routes: (routingHints) ->
		
		
	
LDAPObject = (require "./model.base").LDAPObject

class Exchange extends LDAPObject
	
	@schema =
		"_id": "/type/exchange" 
		"type": "/type/type"
		"name": "Exchange"
		"attributes": 
			"name": { "name": "Name", "required": true, "type": "string" }
			"topology": { "name": "Topology", "required": true, "type": "/type/topology" }
			"host":  { "name": "Host", "required": true, "type": "string" }
			"port": { "name": "Port", "required": true, "type": "number" }
			"vhost": { "name": "VHost", "required": true, "type": "string" }
			"exchange_type": { "name": "Exchange Type", "type": "string" }
	
	type: "/type/exchange"
	objectclass: "exchange"
	
	constructor: (options) ->
		super options
		@host = options.host
		@port = options.port ? 5672
		@vhost = options.vhost ? "/"
		@exchange_type = options.exchange_type
		@topology = options.topology


class ExchangeModule
	
	setRepository: (@repo) ->
	
	getExchange: (id) =>
		@repo._get id
	
	addExchange: (exchange, callback) =>
		topoid = exchange.topology._id ? exchange.topology
		topology = @repo._get topoid
		topology.addReference("exchanges", exchange._id)
		@repo._add exchange, callback
		
	removeExchange: () =>
		
	updateExchange: () =>
		


x = module.exports
x.Exchange = Exchange
x.models = [ Exchange ]
x.extensions = new ExchangeModule
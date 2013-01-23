LDAPObject = (require "./model.base").LDAPObject

x = module.exports

class ProducerRoute extends LDAPObject
	
	@schema =
		"_id": "/type/proute" 
		"type": "/type/type"
		"name": "Producing Route"
		"attributes": 
			"name": { "name": "Name", "type": "string" }
			"topology": { "name": "Topology", "required": true, "type": "/type/topology" }
			"exchange": { "name": "Exchange", "required": true, "type": "/type/exchange" }
			"allow": { "name": "Allowed Principals", "unique": false, "type": "/type/principal" }
			"deny": { "name": "Denied Principals", "unique": false, "type": "/type/principal" }
			"context": { "name": "Context", "type": "object" }

	type: "/type/proute"
	objectclass: "proute"
	
	constructor: (options) ->
		super options
		@context = options.context
		@exchange = options.exchange
		@topology = options.topology
		@allow = options.allow_principals ? []
		@deny = options.deny_principals ? []


class ConsumerRoute extends ProducerRoute
	
	@schema = 
		"_id": "/type/croute" 
		"type": "/type/type"
		"name": "Consuming Route"
		"attributes": 
			"name": { "name": "Name", "type": "string" }
			"topology": { "name": "Topology", "required": true, "type": "/type/topology" }
			"exchange": { "name": "Exchange", "required": true, "type": "/type/exchange" }
			"binding": { "name": "Binding", "required": true, "type": "/type/binding" }
			"allow": { "name": "Allowed Principals", "unique": false, "type": "/type/principal" }
			"deny": { "name": "Denied Principals", "unique": false, "type": "/type/principal" }
			"context": { "name": "Context", "type": "object" }
	
	type: "/type/croute"
	objectclass: "croute"
	
	constructor: (options) ->
		super options
		@binding = options.binding


x.models = [ ProducerRoute, ConsumerRoute ]
x.extensions = {}
LDAPObject = (require "./model.base").LDAPObject

x = module.exports

class Binding extends LDAPObject
	
	@schema = 
		"_id": "/type/binding"
		"type": "/type/type"
		"name": "Binding"
		"attributes": 
			"name": { "name": "Name", "required": true, "type": "string" }
			"routing_key": { "name": "Routing Key", "required": true, "type": "string" }
			"topology": { "name": "Topology", "required": true, "type": "/type/topology" }
			"queue": { "name": "Queue", "type": "string" }
			"durable": { "name": "Durable", "required": true, "type": "boolean" }
			"auto_delete": { "name": "Auto Delete", "required": true, "type": "boolean" }
	
	type: "/type/binding"
	objectclass: "binding"
	
	constructor: (options) ->
		super options
		@routing_key = options.routing_key ? '#'
		@queue = options.queue ? uuid.v4()
		@durable = options.durable ? false
		@auto_delete = options.auto_delete ? true
		@topology = options.topology

x.models = [ Binding ]
x.extensions = {}
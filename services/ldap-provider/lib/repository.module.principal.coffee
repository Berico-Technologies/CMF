LDAPObject = (require "./model.base").LDAPObject

x = module.exports

class Principal extends LDAPObject
	
	@schema = 
		"_id": "/type/principal" 
		"type": "/type/type"
		"name": "Principal"
		"attributes": 
			"name": { "name": "Name", "type": "string", "required": true }
			"suspended": { "name": "Suspended",  "required": true, "type": "boolean" }
	
	type: "/type/principal"
	
	constructor: (options) ->
		super options
		@suspended = options.suspended ? false
		

class Client extends Principal
	
	@schema = 
		"_id": "/type/client" 
		"type": "/type/type"
		"extends": "/type/principal"
		"name": "Client"
		"attributes": 
			"groups": { "name": "Groups", "unique": false, "type": "/type/group" }
	
	type: "/type/client"
	
	constructor: (options) ->
		super options
		@groups = options.groups ? []


class Group extends Principal
	
	@schema = 
		"_id": "/type/group" 
		"type": "/type/type"
		"extends": "/type/principal"
		"name": "Group"
		"attributes": 
			"clients": { "name": "Clients", "unique": false, "type": "/type/client" }
		
	type: "/type/group"
		
	constructor: (options) ->
		super options
		@clients = options.clients ? []


x.models = [ Principal, Client, Group ]
x.extensions = {}
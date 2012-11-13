LDAPObject = (require "./model.base").LDAPObject
conf       = (require "../config")

class Topology extends LDAPObject
	
	@schema = 
		"_id": "/type/topology"
		"type": "/type/type"
		"name": "Topology"
		"attributes":
			"name": { "name": "Name", "type": "string", "required": true }
			"exchanges": { "name": "Exchanges", "type": "/type/exchange" }
			"bindings": { "name": "Bindings", "type": "/type/binding" }
			"proutes": { "name": "Producing Routes", "type": "/type/proute" }
			"croutes": { "name": "Consuming Routes", "type": "/type/croute" }
			"managers": { "name": "Topology Managers", "type": "/type/principal" }
			"parent": { "name": "Parent Topology", "type": "/type/topology" }
			"children": { "name": "Child Topologies", "type": "/type/topology" }
	
	type: "/type/topology"
	objectclass: "topology"
	
	constructor: (options) ->
		super options
		@exchanges = options.exchanges ? []
		@bindings = options.bindings ? []
		@proutes = options.proutes ? []
		@croutes = options.croutes ? []
		@managers = options.managers ? []
		@parent = options.parent ? null
		@children = options.children ? []
		
	__set_id: (options) =>
		if options.parent?
			@dn = @_id = "ou=#{@name}, #{@parent}"
		else
			@dn = @_id = "ou=#{@name}, #{conf.ldap_root}"
		
	__set_topology: (options) =>
		@topology = @

class TopologyModule
	
	setRepository: (@repo) ->
	
	getTopology: (id) =>
		@repo._get id
	
	addTopology: (topology, callback) =>
		@repo._add topology, callback
		
	removeTopology: (topology, callback) =>
		@repo._delete topology._id ? topology, callback
		
	addTopologyManager: () =>
		
	removeTopologyManager: () =>


x = module.exports
x.Topology = Topology
x.models = [ Topology ]
x.extensions = new TopologyModule
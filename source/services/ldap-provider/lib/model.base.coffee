x = module.exports

class x.LDAPObject
	
	constructor: (options) ->
		@name = options.name ? null
		@__set_topology options 
		@__set_id options
	
	__set_id: (options) =>
		@dn = @_id = "cn=#{@name}, #{@topology}"
		
	__set_topology: (options) =>
		@topology = if typeof options.topology is "string" then options.topology else options.topology.dn

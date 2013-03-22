data    = (require "./data-ldap")
_       = (require "underscore")._

class TopologyRepository
	
	@NoOpWriteAheadLog =
		write: (obj, next) -> next(obj)
		read_to_end: (replay_handler) -> return
		compact: (objects) -> null
	
	constructor: (options) ->
		@wal = options.wal
		@graph = new data.Graph()
		for mod in options.modules
			@_extend mod
		@initialize()
				
	initialize: ->
		@wal = TopologyRepository.NoOpWriteAheadLog unless @wal?
		@wal.read_to_end (event) ->
			switch event.type
				when "add" then @_add(event.model)
				when "mod" then @_modify(event.model.id, event.model.attributes)
				when "del" then @_delete(event.model.id)
	
	build_event: (event_type, model) ->
		event = 
			type: event_type
			model: model
			ts: new Date().getTime()
		return event
	
	_add: (model, callback) =>
		that = @
		@wal.write @build_event("add", model), (event) ->
			that.graph.set(event.model)
			callback() if callback?
		
	_modify: (id, attributes, callback) =>
		that = @
		@wal.write @build_event("mod", { id: id, attributes: attributes }), (event) ->
			that.graph.get(event.model.id).set(event.model.attributes)
			callback() if callback?
	
	_delete: (id, callback) =>
		that = @
		@wal.write @build_event("del", { id: id }), (event) ->
			that.graph.del(event.model.id)
			callback() if callback?
	
	_get: (id, show_deleted = false) =>
		node = @graph.get(id)
		if node._deleted?
			if show_deleted then return node else return null
		return node
		
	_extend: (mod) =>
		for model in mod.models
			@graph.set(model.schema)
		if mod.extensions.setRepository?
			mod.extensions.setRepository @
		_.extend @, mod.extensions

	_dump: () =>
		console.log @graph

module.exports.TopologyRepository = TopologyRepository



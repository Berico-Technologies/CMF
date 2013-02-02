_ = require "underscore"

# This is a class that allows the fluent setting and getting of properties
# contained within a property bag.  It creates function for each property
# using the configuration supplied to the constructor.
class Afluent
	
	# Observers that will be called when a property is changed.
	observers: {}
	
	# Build a set of properties based on the configuration
	# provided to the constructor.
	# propertyListName:  name of the variable serving as the property list
	# propertiesConfig:  the configuration for the properties.
	constructor: (propertyListName, propertiesConfig) ->
		if propertyListName?
			@propertyTarget = @[propertyListName]
		else
			@propertyTarget = @
		@propertiesConfig = propertiesConfig ? @propertiesConfig
		me = @
		_.map @propertiesConfig, (propertyConfig) ->
			if propertyConfig.property?
				me[propertyConfig.property] = (value) ->
					return me.getOrSet propertyConfig.key, value
	
	# Get the value of a property, or set the value of a property and return
	# "this" object so you can make a subsequent call to set another value.
	# key: the name of the property to get or set.
	# value: the value of the property (use this only if you are setting the property).
	# returns: the value of the property or "this"
	getOrSet: (key, value) =>
		if value?
			oldValue = @propertyTarget[key]
			@propertyTarget[key] = value
			@fire key, { oldValue: oldValue, newValue: value }
			return @
		return @propertyTarget[key]
	
	# Register an event handler for property change events on a specific
	# property name.
	# property:  Name of the property to register the handler on.
	# handler:  The handler to be fired when the property changes.
	# returns: nothing
	onValueChanged: (property, handler) =>
		@observers[property] = @observers[property] ? []
		@observers[property].push handler
	
	# Fire any handlers registered to the property. 
	# property: The name of the property for which a handler is registered.
	# context: { oldValue: "old value", newValue: "new value" }.
	# returns: nothing
	fire: (property, context) =>
		if @observers[property]?
			_.map @observers[property], (observer) ->
				observer.call observer, context


module.exports = Afluent
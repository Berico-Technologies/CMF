_ = (require "underscore")._

HEADERS =
	ID: "cmf.core.id"
	Type: "cmf.core.type"
	Content_Type: "cmf.core.content-type"


class Envelope
	
	constructor: (@headers = {}, @payload = null) ->
		
	
	get_from_headers: (key) =>
		return @headers[key] ? null
	
	get_id: =>
		return @get_from_headers HEADERS.ID

	get_type: =>
		return @get_from_headers HEADERS.Type
		
	
	get_content_type: =>
		return @get_from_headers HEADERS.Content_Type
		
	


x = exports ? this
x.Envelope = Envelope
x.HEADERS = HEADERS

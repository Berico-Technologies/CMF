ldap = require "ldapjs"
conf = require "../config"


module.exports = (ldap_server, config) ->
	
	ldap_server.search conf.ldap_root, (req, res, next) ->
		if req.dn.toString() isnt 'cn=root' or req.credentials isnt 'secret'
			return next new ldap.InvalidCredentialsError()
		res.end()
		next()
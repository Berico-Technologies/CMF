ldap = require "ldapjs"

module.exports = (ldap_server, config) ->
	
	ldap_server.bind "cn=root", (req, res, next) ->
		if req.dn.toString() isnt "cn=root" or req.credentials isnt config.accounts.root.password
			return next new ldap.InvalidCredentialsError()
		res.end()
		next()
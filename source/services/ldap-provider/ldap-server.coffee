ldap = require "ldapjs"
config = require "./config"

ldap_server = ldap.createServer()

(require "./lib/bind")(ldap_server, config)

ldap_server.listen 1389, () ->
	console.log "Serving requests on port 1389"


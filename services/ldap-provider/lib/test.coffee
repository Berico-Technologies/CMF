line = () -> console.log "------------------------------------------"


modules = [
	require "./repository.module.exchange"
	require "./repository.module.binding"
	require "./repository.module.routes"
	require "./repository.module.topology"
	require "./repository.module.principal"
]

Exchange    = (require "./repository.module.exchange").Exchange
Topology    = (require "./repository.module.topology").Topology
Repository  = (require "./repository.coffee").TopologyRepository

repo = new Repository { modules: modules }

toptions = 
	name: "global"

topology = new Topology toptions

eoptions = 
	name: "security"
	type: "/type/exchange"
	topology: topology._id
	host: "localhost"
	port: 5367
	vhost: "/"
	exchange_type: "direct"
	
exchange = new Exchange eoptions

repo.addTopology topology
repo.addExchange exchange

#console.log (repo._get topology._id).get("exchanges")

#line()

repo.removeTopology topology, () ->
	repo._get topology._id
	
#console.log (repo._get topology._id)

#line()



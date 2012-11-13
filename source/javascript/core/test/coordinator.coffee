vows = require "vows"
assert = require "assert"

Envelope = (require "../envelope").Envelope
Coordinator = (require "../coordinator").Coordinator
conf = (require "../configuration")

vows
	.describe("Coordinator")
	.addBatch                                                        
		
		'Initializes with defaults':                            
		
			topic: new Coordinator(),                          
			'with defaults': (coordinator) ->   
				assert.deepEqual [], coordinator.inbound_chain
				assert.deepEqual [], coordinator.outbound_chain
				assert.equal Coordinator.NoOpTransportReceiver, coordinator.transport_receiver
				assert.equal Coordinator.NoOpTransportSender, coordinator.transport_sender 
		
		'Initializes with supplied state':	
			
			topic: () -> 
				options = 
					inbound_chain: [ (envelope) -> envelope ]
					outbound_chain: [ (envelope) -> envelope ]
					transport_receiver: 
						register: (registration, handler) -> true
						on_envelope_received: (preprocessor) -> true
					transport_sender: 
						send: (envelope) -> envelope
				coordinator = new Coordinator(options)
				return { coordinator: coordinator, options: options }
				
			'with supplied state': (ctx) ->
				assert.deepEqual ctx.options.inbound_chain, ctx.coordinator.inbound_chain 
				assert.deepEqual ctx.options.outbound_chain, ctx.coordinator.outbound_chain 
				assert.equal ctx.options.transport_receiver, ctx.coordinator.transport_receiver 
				assert.equal ctx.options.transport_sender, ctx.coordinator.transport_sender 
				
				
	.export(module)
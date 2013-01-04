CMF - The Common Messaging Framework
=======

CMF is two things:
  - A set of interfaces that describe how software components can publish and subscribe to events
  - An implementation of those interfaces that is extensible, scalable, and secure

CMF takes the opinion that:
  - The client API should be exceedingly simple
  - The client implementation should be extensible and designed with scale in mind
  - Messaging should be fundamentally asynchronous (it can 'act' synchronous if you want it to)
  - The sender and receiver should [conceptually] be unaware of each other (Publish-Subscribe Pattern)



CMF - The Common Messaging Framework
====================================

CMF is a set of interfaces that describe how software components can publish and subscribe to events

CMF takes the opinion that:
  - The client API should be *exceedingly* simple
  - One bus does not fit all.  CMF defines a simple envelope bus and builds focused buses on top of it.
  - The sender and receiver should [conceptually] be unaware of each other (Publish-Subscribe Pattern)


The Interfaces
--------------

```java
public interface IEventBus extends IEventProducer, IEventConsumer
```

Typically, our developers inject the IEventBus or IRpcEventBus interface into their components.  That's 
because most of our components publish AND consume events, and the "bus" interfaces implement both the
producer and the consumer interfaces.  But if you're writing a program that only does one or the other,
you're welcome to merely inject IEventProducer or IEventConsumer into your components.

So remember as you look at the code and examples below, that we're showing the individual interfaces just
for your edification -- we typically just inject one of the bus interfaces and get on with it.


### Producing (aka publishing) events

This is the interface that defines a component that can produce events:

```java
package cmf.bus;

interface IEventProducer
{
  void publish(Object event);
}
```

Here's a bit of code that shows an event producer being injected into your code and then used to produce your event:
```java
class MyEventProducer
{
  IEventProducer producer;
  
  MyEventProducer(IEventProducer eventProducer) {
    this.producer = eventProducer;
  }
  
  void start() {
    // this is your POJO that you want to publish
    MyEvent event = new MyEvent("Hello!");
    
    try {
      this.producer.publish(event);
    }
    catch(Exception ex) {
      // failed to publish the event!
    }
  }
}
```


### Consuming (aka subscribing to) events

This is the interface that defines a component that can consume (or subscribe to) events:
```java
package cmf.bus;

interface IEventConsumer
{
  void subscribe(IEventHandler<T> handler);
  void subscribe(IEventHandler<T> handler, IEventFilterPredicate predicate);
}
```

Here's a bit of code that shows an event consumer being injected into your code and then used to consume events:
```java
class MyEventConsumer
{
  IEventConsumer consumer;
  
  MyEventConsumer(IEventConsumer eventConsumer) {
    this.consumer = eventConsumer;
  }
  
  void start() {
    this.consumer.subscribe(new IEventHandler<MyEvent>() {
      @Override getEvent() { return MyEvent.class; }
      
      @Override void handle(MyEvent event, Map<string, string> headers) {
        // I got an event!
      }
      
      @Override handleFailed(Envelope env, Exception ex) {
        // this envelope contains a MyEvent, but the consumer couldn't get it from the envelope
        // we can check the exception to see what happened
      }
    });
  }
}
```

Honestly?  The only other thing you need to know in order to start producing and consuming events is how to 
configure your IoC container in order to wire up an implementation.  But even without that knowledge, you could begin writing (and unit testing!) software components written against the interfaces!

Common Questions
----------------

### Aren't the event bus interfaces a bit *too* simple?

We don't think so, and we think that our opinion is justified by our experiences in using it.  If you have a list
of scenarios that you think our interfaces can't handle, you're probably approaching the event bus expecting it
to be a kafka/rabbit/mule kind of messaging/esb framework instead of a bus for passing [Domain Events](https://www.google.com/search?q=domain+event).

### But the name of this project is common *messaging* framework!!

Yes indeed.  See, we've written a very thin wrapper around a messaging framework (RabbitMQ), and the "event bus" 
*uses* that thin wrapper to actually send and receive domain events.  We call it the Envelope Bus, and it's been
designed for you to use -- just like the event bus uses it.

### Ok, I'm confused.  Can you show me a picture?

Yep.  This should clear things up a bit.

![There is supposed to be an image of the conceptual architecture here](https://dl.dropbox.com/u/12311372/CMF_Conceptual.png "CMF Implementation")

### That makes more sense.  Which should I use?

Is your architecture following the Domain Events pattern?  Or are you just trying to send and receive events? If 
so, use the event bus.  If you have a scenario like "I want to send and receive media, not events!", you should
either use the envelope bus or write your own "Media Bus" on top of our envelope bus.  Another example?  User 
Notification.  If you're just doing simple user notification, you can make clever use of the event bus.  But if 
you have more sophisticated requirements, write your own "User Notification Bus" on top of the envelope bus.

### If I'm writing my own "bus" anyway, why use CMF at all?

Ah, now we come to one of the really beautiful value propositions that CMF has to offer.  Since CMF is merely an abstraction, you're free to find and use the best implementation for your needs.  It's likely that a bus has already been written for you.  Already, we know of command buses, streaming buses, batched event buses, web-socket buses, etcetera that implement CMF.

For example, check out the implementation that we've written: [Open AMPere](http://github.com/Berico-Technologies/AMP).

Here's just a few of the feature of our implementation:
 - Multi-lingual Clients
   - Java Client
   - .NET Client
   - Client-Side JavaScript Client
   - Coming Soon: Python and C++!!
 - Routing is flexible and dynamic. 
   - Send to - or listen on - multiple clusters of queues/exchanges for a given message type
   - Change routing at runtime without stopping anything
   - Redirect message traffic (at runtime) in the event of performance bottlenecks
   - Configure secondary brokers in the event of a crash by the primary brokers
 - Governance
   - Target certain message types to be passed through a policy interdiction point before proceeding onto consumers
   - Inspect the stream of messages at a system-wide level in order to recognize more complex events
   - Record selected messages for system-wide auditing
 - Security
   - Envelope payloads can be signed to provide message integrity and authenticity
   - Envelope payloads can be encrypted to provide message confidentiality

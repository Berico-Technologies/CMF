CMF - The Common Messaging Framework
=======

CMF is two things:
  - A set of interfaces that describe how software components can publish and subscribe to events
  - An implementation of those interfaces that is extensible, scalable, and secure

CMF takes the opinion that:
  - The client API should be exceedingly simple
  - The client implementation should be extensible and designed with scale and fault tolerance in mind
  - Messaging should be fundamentally asynchronous (it can 'act' synchronous if you want it to)
  - The sender and receiver should [conceptually] be unaware of each other (Publish-Subscribe Pattern)


-- The Interfaces --

```java
public interface IEventBus extends IEventProducer, IEventConsumer
```

Typically, our developers inject the IEventBus or IRpcEventBus interface into their components.  That's 
because most of our components publish AND consume events, and the "bus" interfaces implement both the
producer and the consumer interfaces.  But if you're writing a program that only does one or the other,
you're welcome to merely inject IEventProducer or IEventConsumer into your components.

So remember as you look at the code and examples below, that we're showing the individual interfaces just
for your edification.


Producing (aka publishing) events

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


Consuming (aka subscribing to) events

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
configure your IoC container in order to wire up the implementation that comes with CMF.  But even without 
that knowledge, you could begin writing (and unit testing!) software components written against the interfaces!

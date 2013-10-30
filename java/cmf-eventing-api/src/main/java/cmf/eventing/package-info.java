/**
 * <p>The cmf.eventing package defines the interfaces, and their dependent types, 
 * needed to publish and subscribe to events on an event bus.  Events in CMF are 
 * simply plain-old-java-objects (POJOs) and are strongly typed.  Inheritance 
 * however is intentionally not supported.  That is subscribing to an "Alert"
 * event will not get you instances of the "Critial Alert" sub-type, should 
 * any be published.</p>
 *
 * <p>Implementations of this package are presumed to use an implementation of the
 * cmf.bus package under the hood.  That is to say, eventing implementations 
 * should by one means or another transform all POJO events to {@link cmf.bus.Envelope}s 
 * that are sent using an {@link cmf.bus.IEnvelopeSender} and should receive all 
 * events as Envelopes from an {@link cmf.bus.IEnvelopeReceiver} and transform 
 * them back into the appropriate strongly typed POJOs.</p>
 */
package cmf.eventing;

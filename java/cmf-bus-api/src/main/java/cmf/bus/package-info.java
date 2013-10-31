/**
 * <p>The cmf.bus package defines the interfaces, and their dependent types, 
 * needed to send and receive messages on a message bus.  The central type 
 * of the package is the {@link cmf.bus.Envelope}.  An Envelope is the CMF representation 
 * of a message in its most basic form.  All other members of the cmf.bus 
 * package concern themselves with sending and receiving envelopes.</p>
 * 
 * <p>Sending and receiving envelopes on a bus is the most primitive construct
 * in CMF.  All other CMF APIs are generally intended in their implementation
 * to utilize the cmf.bus API as their transport and act as higher level
 * abstractions on top of it.</p>
 */
package cmf.bus;
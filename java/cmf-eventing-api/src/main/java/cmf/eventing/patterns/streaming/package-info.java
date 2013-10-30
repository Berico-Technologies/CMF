/**
 * <p>The cmf.eventing.patterns.streaming package is an extension of the 
 * cmf.eventing package that defines additional interfaces, and their dependent
 * types, useful for sending large amounts of data as a stream of related 
 * events, referred to as an event-stream.  </p>
 * <p>The APIs support two basic forms of streaming, one in which events in the 
 * stream are published and received individually, and one in which all events 
 * in the stream are published at once and handled at once.  The former mode is 
 * most useful when needing to send data that is being generated or retrieved 
 * incrementally and where the total amount of data to be sent is not known in 
 * advance.  It allows receivers of the data to process each block (event) of 
 * data in the stream as it becomes available. The latter mode is most useful 
 * when all the data is available at once at the producing end, and can be 
 * processed all at once on the consuming end, but the total data size is such 
 * that sending it as a single event may be prohibitive.</p>
 */
package cmf.eventing.patterns.streaming;

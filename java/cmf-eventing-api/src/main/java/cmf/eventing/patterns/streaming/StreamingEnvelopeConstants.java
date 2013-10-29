package cmf.eventing.patterns.streaming;

/**
 * Defines several constants that are used as {@link cmf.bus.Envelope} 
 * header keys for headers that provide stream specific metadata.
 * 
 * WARNING: The streaming event API and its accompanying implementation is deemed 
 * to be a proof of concept at this point and subject to change.  It should not be used 
 * in a production environment. 
 */
public class StreamingEnvelopeConstants {
	/**
	 * Defines the key for the header containing the unique identifier for the stream
	 * to which the given event belongs.
	 */
    public static final String SEQUENCE_ID = "cmf.bus.message.pattern#streaming.sequenceId";
    
    /**
     * Defines the key for the header containing the ordinal value indicating the
     * current events position within the stream.
     */
    public static final String POSITION = "cmf.bus.message.pattern#streaming.position";
}

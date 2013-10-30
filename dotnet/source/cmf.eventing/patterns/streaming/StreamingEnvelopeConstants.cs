namespace cmf.eventing.patterns.streaming
{
    /// <summary> 
    /// Defines several constants that are used as Envelope header keys for headers that provide stream specific metadata.
    /// <para>WARNING: The streaming event API and its accompanying implementation is deemed 
    /// to be a proof of concept at this point and subject to change.  It should not be used 
    /// in a production environment. </para>
    /// </summary>
    public class StreamingEnvelopeConstants
    {
        /// <summary>
        /// Defines the key for the header containing the unique identifier for the stream to which the given event belongs.
        /// </summary>
        public const string SEQUENCE_ID = "cmf.bus.message.pattern#streaming.sequenceId";
     
        /// <summary>
        /// Defines the key for the header containing the ordinal value indicating the current events position within the stream.
        /// </summary>
        public const string POSITION = "cmf.bus.message.pattern#streaming.position";
    }
}

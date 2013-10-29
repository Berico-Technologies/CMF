using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing.patterns.streaming
{
    /// <summary> 
    /// WARNING: The streaming event API and its accompanying implementation is deemed 
    /// to be a proof of concept at this point and subject to change.  It should not be used 
    /// in a production environment. 
    /// </summary>
    public class StreamingEnvelopeConstants
    {
        public const string SEQUENCE_ID = "cmf.bus.message.pattern#streaming.sequenceId";
        public const string POSITION = "cmf.bus.message.pattern#streaming.position";
    }
}

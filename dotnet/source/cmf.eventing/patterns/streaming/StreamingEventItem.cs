using System;
using System.Collections.Generic;
using SEC = cmf.eventing.patterns.streaming.StreamingEnvelopeConstants;

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// This is a wrapper class that wraps each event received as part of an event stream 
    /// in addition to providing access to the wrapped event, it also provides access to 
    /// additional stream specific meta data.
    /// 
    /// <para>WARNING: The streaming event API and its accompanying implementation is deemed 
    /// to be a proof of concept at this point and subject to change.  It should not be used 
    /// in a production environment.</para>
    /// </summary>
    public class StreamingEventItem<TEvent>
    {
        private readonly TEvent _streamEvent;
        private readonly IDictionary<string, string> _eventHeaders;

        /// <summary>
        /// The event received from the published stream
        /// </summary>
        public TEvent Event
        {
            get
            {
                return _streamEvent;
            }
        }

        /// <summary>
        /// The sequenceId value which is common to all events in the same stream.
        /// </summary>
        public Guid SequenceId
        {
            get
            {
                return Guid.Parse(_eventHeaders[SEC.SEQUENCE_ID]);
            }
        }

        /// <summary>
        /// The ordinal position of this event within the event stream.
        /// </summary>
        public int Position
        {
            get
            {
                return int.Parse(_eventHeaders[SEC.POSITION]);
            }
        }

        /// <summary>
        /// The headers associated with the received event.
        /// </summary>
        public IDictionary<string, string> EventHeaders
        {
            get
            {
                return _eventHeaders;
            }
        }

        public StreamingEventItem(TEvent streamEvent, IDictionary<string, string> eventHeaders)
        {
            _streamEvent = streamEvent;
            _eventHeaders = eventHeaders;
        }

    }
}

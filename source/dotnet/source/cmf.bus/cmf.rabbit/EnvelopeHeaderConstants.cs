using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.rabbit
{
    public static class EnvelopeHeaderConstants
    {
        public static readonly string MESSAGE_TOPIC = "cmf.bus.message.topic";
        public static readonly string MESSAGE_TYPE = "cmf.bus.message.type";
        public static readonly string MESSAGE_ID = "cmf.bus.message.id";
        public static readonly string MESSAGE_CORRELATION_ID = "cmf.bus.message.correlation_id";
    }
}

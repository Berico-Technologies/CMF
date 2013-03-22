using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus
{
    public static class EnvelopeHeaderConstants
    {
        public static readonly string MESSAGE_TOPIC = "cmf.bus.message.topic";
        public static readonly string MESSAGE_TYPE = "cmf.bus.message.type";
        public static readonly string MESSAGE_ID = "cmf.bus.message.id";
        public static readonly string MESSAGE_CORRELATION_ID = "cmf.bus.message.correlation_id";
        public static readonly string MESSAGE_PATTERN = "cmf.bus.message.pattern";
        public static readonly string MESSAGE_PATTERN_PUBSUB = "cmf.bus.message.pattern#pub_sub";
        public static readonly string MESSAGE_PATTERN_RPC = "cmf.bus.message.pattern#rpc";
        public static readonly string MESSAGE_PATTERN_RPC_TIMEOUT = "cmf.bus.message.pattern#rpc.timeout";
        public static readonly string MESSAGE_SENDER_IDENTITY = "cmf.bus.message.sender_identity";
        public static readonly string MESSAGE_SENDER_SIGNATURE = "cmf.bus.message.sender_signature";
        public static readonly string ENVELOPE_CREATION_TIME = "cmf.bus.envelope.creation";
        public static readonly string ENVELOPE_RECEIPT_TIME = "cmf.bus.envelope.receipt";
    }
}

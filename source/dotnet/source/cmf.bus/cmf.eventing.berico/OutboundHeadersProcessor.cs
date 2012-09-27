using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus.berico;

namespace cmf.eventing.berico
{
    class OutboundHeadersProcessor : IOutboundEventProcessor
    {
        public void ProcessOutbound(ref object ev, ref bus.Envelope env, IDictionary<string, object> context)
        {
            Guid messageId = env.GetMessageId();
            messageId = Guid.Equals(Guid.Empty, messageId) ? Guid.NewGuid() : messageId;
            env.SetMessageId(messageId.ToString());

            string messageType = env.GetMessageType();
            messageType = string.IsNullOrEmpty(messageType) ? ev.GetType().FullName : messageType;
            env.SetMessageType(messageType);

            string messageTopic = env.GetMessageTopic();
            messageTopic = string.IsNullOrEmpty(messageTopic) ? ev.GetType().FullName : messageTopic;
            env.SetMessageTopic(messageTopic);
        }
    }
}

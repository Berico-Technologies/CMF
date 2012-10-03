using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus.berico;

namespace cmf.eventing.berico
{
    public class OutboundHeadersProcessor : IOutboundEventProcessor
    {
        public virtual void ProcessOutbound(ref object ev, ref bus.Envelope env, IDictionary<string, object> context)
        {
            Guid messageId = env.GetMessageId();
            messageId = Guid.Equals(Guid.Empty, messageId) ? Guid.NewGuid() : messageId;
            env.SetMessageId(messageId.ToString());

            Guid correlationId = env.GetCorrelationId();

            string messageType = env.GetMessageType();
            messageType = string.IsNullOrEmpty(messageType) ? ev.GetType().FullName : messageType;
            env.SetMessageType(messageType);

            string messageTopic = env.GetMessageTopic();
            messageTopic = string.IsNullOrEmpty(messageTopic) ? ev.GetType().FullName : messageTopic;
            if (Guid.Empty != correlationId)
            {
                messageTopic = messageTopic + "#" + correlationId.ToString();
            }
            env.SetMessageTopic(messageTopic); 
        }
    }
}

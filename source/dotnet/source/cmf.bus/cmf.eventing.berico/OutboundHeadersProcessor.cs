using System;
using System.Collections.Generic;
using System.DirectoryServices.AccountManagement;
using System.Linq;
using System.Text;

using cmf.bus;

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
            messageType = string.IsNullOrEmpty(messageType) ? this.GetMessageType(ev) : messageType;
            env.SetMessageType(messageType);

            string messageTopic = env.GetMessageTopic();
            messageTopic = string.IsNullOrEmpty(messageTopic) ? this.GetMessageTopic(ev) : messageTopic;
            if (Guid.Empty != correlationId)
            {
                messageTopic = messageTopic + "#" + correlationId.ToString();
            }
            env.SetMessageTopic(messageTopic);

            string senderIdentity = env.GetSenderIdentity();
            senderIdentity = string.IsNullOrEmpty(senderIdentity) ? UserPrincipal.Current.DistinguishedName.Replace(",", ", ") : senderIdentity;
            env.SetSenderIdentity(senderIdentity);
        }

        public string GetMessageTopic(object ev)
        {
            string topic = ev.GetType().FullName;

            try
            {
                object[] attributes = ev.GetType().GetCustomAttributes(typeof(EventAttribute), true);
                EventAttribute attr = attributes.OfType<EventAttribute>().FirstOrDefault();
                if (null != attr)
                {
                    topic = attr.EventTopic;
                }
            }
            catch { }

            return topic;
        }

        public string GetMessageType(object ev)
        {
            string type = ev.GetType().FullName;

            try
            {
                object[] attributes = ev.GetType().GetCustomAttributes(typeof(EventAttribute), true);
                EventAttribute attr = attributes.OfType<EventAttribute>().FirstOrDefault();
                if (null != attr)
                {
                    type = attr.EventType;
                }
            }
            catch { }

            return type;
        }
    }
}

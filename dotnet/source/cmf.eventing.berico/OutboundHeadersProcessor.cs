using System;
using System.Collections.Generic;
using System.DirectoryServices.AccountManagement;
using System.Linq;
using System.Text;

using cmf.bus;
using Common.Logging;

namespace cmf.eventing.berico
{
    public class OutboundHeadersProcessor : IEventProcessor
    {
        protected ILog _log = LogManager.GetLogger(typeof(OutboundHeadersProcessor));

        public virtual void ProcessEvent(EventContext context, Action continueProcessing)
        {
            // only process outbound events
            if (context.Direction != EventContext.Directions.Out) { continueProcessing(); }

            Envelope env = context.Envelope;

            Guid messageId = env.GetMessageId();
            messageId = Guid.Equals(Guid.Empty, messageId) ? Guid.NewGuid() : messageId;
            env.SetMessageId(messageId.ToString());

            Guid correlationId = env.GetCorrelationId();

            string messageType = env.GetMessageType();
            messageType = string.IsNullOrEmpty(messageType) ? this.GetMessageType(context.Event) : messageType;
            env.SetMessageType(messageType);

            string messageTopic = env.GetMessageTopic();
            messageTopic = string.IsNullOrEmpty(messageTopic) ? this.GetMessageTopic(context.Event) : messageTopic;
            if (Guid.Empty != correlationId)
            {
                messageTopic = messageTopic + "#" + correlationId.ToString();
            }
            env.SetMessageTopic(messageTopic);

            string senderIdentity = env.GetSenderIdentity();
            senderIdentity = string.IsNullOrEmpty(senderIdentity) ? UserPrincipal.Current.DistinguishedName.Replace(",", ", ") : senderIdentity;
            env.SetSenderIdentity(senderIdentity);

            continueProcessing();
        }

        public string GetMessageTopic(object ev)
        {
            string topic = ev.GetType().FullName;

            try
            {
                object[] attributes = ev.GetType().GetCustomAttributes(typeof(EventAttribute), true);
                EventAttribute attr = attributes.OfType<EventAttribute>().FirstOrDefault();
                if ((null != attr) && (false == string.IsNullOrEmpty(attr.EventTopic)))
                {
                    _log.Debug("EventAttribute: " + attr.EventTopic);
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
                if ((null != attr) && (false == string.IsNullOrEmpty(attr.EventType)))
                {
                    _log.Debug("EventAttribute: " + attr.EventType);
                    type = attr.EventType;
                }
            }
            catch { }

            return type;
        }
    }
}

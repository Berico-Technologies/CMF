using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Common.Logging;

using cmf.bus.berico;

namespace cmf.eventing.berico
{
    public class OutboundHeadersProcessor : IOutboundEventProcessor
    {
        protected ILog _log;


        public OutboundHeadersProcessor()
        {
            _log = LogManager.GetLogger(this.GetType());
        }


        public virtual void ProcessOutbound(ref object ev, ref bus.Envelope env, IDictionary<string, object> context)
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

            _log.Debug(string.Format(
                "Outgoing headers: {{type:{0},topic:{1},id:{2}}}", 
                messageType, 
                messageTopic, 
                messageId.ToString()));
        }
    }
}

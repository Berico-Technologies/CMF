using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Newtonsoft.Json;

using cmf.bus;
using cmf.bus.berico;

namespace cmf.eventing.berico.serializers
{
    public class JsonEventSerializer : IInboundEventProcessor, IOutboundEventProcessor
    {
        public void ProcessInbound(ref object ev, ref Envelope env, ref IDictionary<string, object> context)
        {
            string eventTopic = env.GetMessageTopic();
            Type eventType = Type.GetType(env.GetMessageTopic());
            ev = JsonConvert.DeserializeObject(Encoding.UTF8.GetString(env.Payload), eventType);
        }

        public void ProcessOutbound(ref object ev, ref Envelope env, IDictionary<string, object> context)
        {
        }
    }
}

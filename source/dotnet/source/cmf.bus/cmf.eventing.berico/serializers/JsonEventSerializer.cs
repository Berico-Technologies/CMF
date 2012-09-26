using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
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
            // get the topic from the headers on the envelope
            string eventTopic = env.GetMessageTopic();
            // and make sure we get something
            if (string.IsNullOrEmpty(eventTopic))
            {
                throw new Exception("Cannot deserialize an envelope that does not specify the event's topic");
            }


            // start with a null Type
            Type eventType = null;

            // go through each assembly loaded into the app domain
            foreach (Assembly ass in AppDomain.CurrentDomain.GetAssemblies())
            {
                // and see if it can get us our Type
                eventType = ass.GetType(eventTopic);
                if (null != eventType) { break; }
            }


            if (null != eventType) // if we did get a Type, we can deserialize the event
            {
                ev = JsonConvert.DeserializeObject(Encoding.UTF8.GetString(env.Payload), eventType);
            }
            else // otherwise, throw an exception
            {
                throw new Exception("Cannot deserialize an event of topic '" + eventTopic + "' because no Type could be found for it");
            }
        }

        public void ProcessOutbound(ref object ev, ref Envelope env, IDictionary<string, object> context)
        {
            // first, serialize the event (make it pretty!)
            string json = JsonConvert.SerializeObject(ev, Formatting.Indented);

            // next, convert the string into bytes using UTF-8
            env.Payload = new UTF8Encoding().GetBytes(json);

            // finally, store the event's Type on the envelope's header
            env.SetMessageType(ev.GetType().FullName);
        }
    }
}

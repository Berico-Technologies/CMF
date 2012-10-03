using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;

using Common.Logging;
using Newtonsoft.Json;

using cmf.bus;
using cmf.bus.berico;

namespace cmf.eventing.berico.serializers
{
    public class JsonEventSerializer : IInboundEventProcessor, IOutboundEventProcessor
    {
        protected ILog _log;


        public JsonEventSerializer()
        {
            _log = LogManager.GetLogger(this.GetType());
        }


        public virtual bool ProcessInbound(ref object ev, ref Envelope env, ref IDictionary<string, object> context)
        {
            bool success = false;

            try
            {
                // get the type from the headers on the envelope
                string eventType = env.GetMessageType();

                // if no type, try the topic
                if (string.IsNullOrEmpty(eventType))
                {
                    eventType = env.GetMessageTopic();
                }

                // and make sure we get something
                if (string.IsNullOrEmpty(eventType))
                {
                    throw new Exception("Cannot deserialize an envelope that does not specify the event's topic");
                }


                // start with a null Type
                Type type = null;

                // go through each assembly loaded into the app domain
                foreach (Assembly ass in AppDomain.CurrentDomain.GetAssemblies())
                {
                    // and see if it can get us our Type
                    type = ass.GetType(eventType);
                    if (null != type)
                    {
                        _log.Debug("Found type " + type + " in assembly " + ass.FullName);
                        break;
                    }
                }


                if (null != type) // if we did get a Type, we can deserialize the event
                {
                    string jsonString = Encoding.UTF8.GetString(env.Payload);
                    _log.Debug("Will attempt to deserialize: " + jsonString);
                    ev = JsonConvert.DeserializeObject(jsonString, type);

                    success = true;
                }
                else // otherwise, throw an exception
                {
                    throw new Exception("Cannot deserialize an event of topic '" + type + "' because no Type could be found for it");
                }
            }
            catch (Exception ex)
            {
                _log.Error("Failed to deserialize an event", ex);
            }

            return success;
        }

        public virtual void ProcessOutbound(ref object ev, ref Envelope env, IDictionary<string, object> context)
        {
            try
            {
                // first, serialize the event (make it pretty!)
                string json = JsonConvert.SerializeObject(ev, Formatting.Indented);

                _log.Debug("Serialized event: " + json);

                // next, convert the string into bytes using UTF-8
                env.Payload = new UTF8Encoding().GetBytes(json);
            }
            catch (Exception ex)
            {
                _log.Error("Failed to serialize an event", ex);
            }
        }
    }
}

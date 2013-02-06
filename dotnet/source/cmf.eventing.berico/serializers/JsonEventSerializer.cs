using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;

using Common.Logging;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

using cmf.bus;
using cmf.bus.berico;

namespace cmf.eventing.berico.serializers
{
    public class JsonEventSerializer : IEventProcessor
    {
        /// <summary>
        /// We need to resolve properties to lowercase in order to be interoperable
        /// with java and other json serializers.
        /// </summary>
        protected class LowercaseContractResolver : DefaultContractResolver
        {
            protected override string ResolvePropertyName(string propertyName)
            {
                string firstChar = propertyName.Substring(0, 1).ToLower();
                string camelName = string.Format("{0}{1}", firstChar, propertyName.Substring(1));

                return camelName;
            }
        }


        protected ILog _log;
        protected JsonSerializerSettings _settings;


        public JsonEventSerializer()
        {
            _log = LogManager.GetLogger(this.GetType());

            _settings = new JsonSerializerSettings();
            _settings.ContractResolver = new LowercaseContractResolver();
            _settings.TypeNameHandling = TypeNameHandling.Objects;
        }


        public virtual void ProcessEvent(EventContext context, Action continueProcessing)
        {
            if (EventContext.Directions.In == context.Direction)
            {
                this.ProcessInbound(context, continueProcessing);
            }
            if (EventContext.Directions.Out == context.Direction)
            {
                this.ProcessOutbound(context, continueProcessing);
            }
        }

        public virtual void ProcessInbound(EventContext context, Action continueProcessing)
        {
            bool success = false;
            Envelope env = context.Envelope;

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
                StringBuilder assembliesLoaded = new StringBuilder();
                foreach (Assembly assembly in AppDomain.CurrentDomain.GetAssemblies())
                {
                    assembliesLoaded.AppendLine("Loaded Assembly Found: " + assembly.FullName);

                    // and see if it can get us our Type
                    type = assembly.GetType(eventType);
                    if (null != type)
                    {
                        _log.Debug("Found type " + type + " in assembly " + assembly.FullName);
                        break;
                    }
                }
                _log.Debug("Currently Loaded Assemblies:" + "\r\n" + assembliesLoaded.ToString());

                if (null != type) // if we did get a Type, we can deserialize the event
                {

                    string jsonString = new UTF8Encoding().GetString(env.Payload); //Encoding.UTF8.GetString(env.Payload);
                    _log.Debug("Will attempt to deserialize: " + jsonString);
                    context.Event = JsonConvert.DeserializeObject(jsonString, type, _settings);

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

            if (success) { continueProcessing(); }
        }

        public virtual void ProcessOutbound(EventContext context, Action continueProcessing)
        {
            object ev = context.Event;

            try
            {
                // first, serialize the event (make it pretty!)
                string json = JsonConvert.SerializeObject(ev, Formatting.Indented, _settings);

                _log.Debug("Serialized event: " + json);

                // next, convert the string into bytes using UTF-8
                context.Envelope.Payload = new UTF8Encoding().GetBytes(json);
            }
            catch (Exception ex)
            {
                _log.Error("Failed to serialize an event", ex);
                throw;
            }

            continueProcessing();
        }
    }
}

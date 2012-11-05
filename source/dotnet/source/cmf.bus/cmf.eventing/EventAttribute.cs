using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing
{
    public class EventAttribute : Attribute
    {
        public string EventType { get; protected set; }
        public string EventTopic { get; protected set; }


        public EventAttribute(string eventTopic = null, string eventType = null)
        {
            this.EventType = eventType;
            this.EventTopic = eventTopic;
        }
    }
}

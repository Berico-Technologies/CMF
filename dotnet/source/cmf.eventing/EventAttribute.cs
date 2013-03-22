using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing
{
    public class EventAttribute : Attribute
    {
        public string EventType { get; set; }
        public string EventTopic { get; set; }


        public EventAttribute()
        {
        }

        public EventAttribute(string eventTopic = null, string eventType = null)
        {
            this.EventType = eventType;
            this.EventTopic = eventTopic;
        }
    }
}

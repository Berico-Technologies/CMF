using System;
using cmf.bus.support;

namespace cmf.eventing
{
    /// <summary>
    /// Used to annotate types that may be send as Events.
    /// </summary>
    public class EventAttribute : Attribute
    {
        /// <summary>
        /// The type of the event.
        /// </summary>
        public string EventType { get; set; }

        /// <summary>
        /// The topic of the event.
        /// </summary>
        public string EventTopic { get; set; }

        /// <summary>
        /// Initializes a new instance of the EventAttribute class
        /// </summary>
        public EventAttribute()
        {
            var bus = new InMemoryEnvelopeBus();
            bus.Send(null);
        }

        /// <summary>
        /// Initializes a new instance of the EventAttribute class with a specific topic and event type.
        /// </summary>
        /// <param name="eventTopic">The topic of the event.</param>
        /// <param name="eventType">The type of the event.</param>
        public EventAttribute(string eventTopic = null, string eventType = null)
        {
            this.EventType = eventType;
            this.EventTopic = eventTopic;
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;

namespace cmf.eventing.berico
{
    public class EventContext
    {
        public enum Directions { In, Out }


        public Directions Direction { get; protected set; }
        public Envelope Envelope { get; set; }
        public object Event { get; set; }
        public IDictionary<string, object> Properties { get; set; }

        public object this[string key]
        {
            get
            {
                object value = null;

                if (this.Properties.ContainsKey(key))
                {
                    value = this.Properties[key];
                }

                return value;
            }
            set
            {
                this.Properties[key] = value;
            }
        }


        public EventContext(Directions direction)
        {
            this.Direction = direction;
            this.Properties = new Dictionary<string, object>();
        }

        public EventContext(Directions direction, Envelope envelope)
            : this(direction)
        {
            this.Envelope = envelope;
        }

        public EventContext(Directions direction, Envelope envelope, object ev)
            : this(direction, envelope)
        {
            this.Event = ev;
        }
    }
}

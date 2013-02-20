using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.berico
{
    public class EnvelopeContext
    {
        public enum Directions { In, Out }
        

        public Envelope Envelope { get; set; }
        public IDictionary<string, object> Properties { get; set; }
        public Directions Direction { get; set; }


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


        public EnvelopeContext(Directions direction)
        {
            this.Properties = new Dictionary<string, object>();

            this.Direction = direction;
        }

        public EnvelopeContext(Directions direction, Envelope envelope)
            : this(direction)
        {
            this.Envelope = envelope;
        }
    }
}

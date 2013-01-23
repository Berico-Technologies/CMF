using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.berico
{
    public class EnvelopeContext
    {
        public Envelope Envelope { get; set; }
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


        public EnvelopeContext()
        {
            this.Properties = new Dictionary<string, object>();
        }

        public EnvelopeContext(Envelope envelope)
            : this()
        {
            this.Envelope = envelope;
        }
    }
}

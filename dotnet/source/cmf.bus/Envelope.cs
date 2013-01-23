using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus
{
    public sealed class Envelope
    {
        public IDictionary<string, string> Headers { get; set; }
        public byte[] Payload { get; set; }


        public Envelope()
        {
            this.Headers = new Dictionary<string, string>();
        }

        public Envelope(IDictionary<string, string> headers, byte[] payload)
        {
            this.Headers = headers;
            this.Payload = payload;
        }


        public override string ToString()
        {
            StringBuilder sb = new StringBuilder("{");

            if (this.Headers.Count > 0)
            {
                KeyValuePair<string, string> first = this.Headers.First();
                sb.AppendFormat("\"{0}\":\"{1}\"", first.Key, first.Value);
 
                for (int index = 1; index < this.Headers.Count(); index++)
                {
                    sb.AppendFormat(",\"{0}\":\"{1}\"", first.Key, first.Value);
                }
            }

            sb.Append("}");

            return sb.ToString();
        }
    }
}

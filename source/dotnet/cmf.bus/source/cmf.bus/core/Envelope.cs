using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.core
{
    public sealed class Envelope
    {
        public IDictionary<string, string> Headers { get; set; }
        public byte[] Payload { get; set; }
    }
}

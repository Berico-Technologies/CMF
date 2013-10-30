using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus
{
    /// <summary>
    /// Represents a discreet message sent across the bus. Envelopes consist of two parts, 
    /// payload represented as an array of bytes and optionally a set of headers represented 
    /// as string/string map of key/value pairs. Together they comprise the message and any 
    /// appropriate meta-data pertaining to it respectively.
    /// </summary>
    public sealed class Envelope
    {
        /// <summary>
        ///  The envelope's set of headers.
        /// </summary>
        public IDictionary<string, string> Headers { get; set; }

        /// <summary>
        /// The envelope's payload.
        /// </summary>
        public byte[] Payload { get; set; }

        /// <summary>
        /// Initializes a new instance of the Envelope class.
        /// </summary>
        public Envelope()
        {
            this.Headers = new Dictionary<string, string>();
        }

        /// <summary>
        /// Initializes a new instance of the Envelope class with specific headers and payload.
        /// </summary>
        /// <param name="headers">The headers for the envelope.</param>
        /// <param name="payload">The payload of the envelope.</param>
        public Envelope(IDictionary<string, string> headers, byte[] payload)
        {
            this.Headers = headers;
            this.Payload = payload;
        }

        /// <summary>
        /// Returns a string representation of the envelope that lists the key/value pairs contained in the envelope's headers.
        /// </summary>
        /// <returns>A string representation of the envelope.</returns>
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

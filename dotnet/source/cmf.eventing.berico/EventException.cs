using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;

namespace cmf.eventing.berico
{
    public class EventException : Exception
    {
        public EventException()
        {
        }

        public EventException(string message) : base(message)
        {
        }

        public EventException(string message, Exception innerException) : base(message, innerException)
        {
        }

        protected EventException(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}

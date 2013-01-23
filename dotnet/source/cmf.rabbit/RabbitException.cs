using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;

namespace cmf.rabbit
{
    public class RabbitException : Exception
    {
        public RabbitException()
            : base()
        { }

        public RabbitException(string message)
            : base(message)
        { }

        public RabbitException(string message, Exception innerException)
            : base(message, innerException)
        { }

        protected RabbitException(SerializationInfo info, StreamingContext context)
            : base(info, context)
        { }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;

namespace cmf.bus.berico
{
    public static class Extensions
    {
        /**
         * On setters, use the IDictionary.Item property instead of the Add method
         * http://msdn.microsoft.com/en-us/library/9tee9ht2%28v=vs.100%29
         */


        public static string GetMessageTopic(this Envelope env)
        {
            string msgType = null;

            if (env.Headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_TOPIC))
            {
                msgType = env.Headers[EnvelopeHeaderConstants.MESSAGE_TOPIC];
            }

            return msgType;
        }

        public static void SetMessageTopic(this Envelope env, string messageTopic)
        {
            env.Headers[EnvelopeHeaderConstants.MESSAGE_TOPIC] = messageTopic;
        }

        public static string GetMessageTopic(this IDictionary<string, string> headers)
        {
            string msgType = null;

            if (headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_TOPIC))
            {
                msgType = headers[EnvelopeHeaderConstants.MESSAGE_TOPIC];
            }

            return msgType;
        }

        public static void SetMessageTopic(this IDictionary<string, string> headers, string messageTopic)
        {
            headers[EnvelopeHeaderConstants.MESSAGE_TOPIC] = messageTopic;
        }


        public static Guid GetMessageId(this Envelope env)
        {
            Guid id = Guid.Empty;

            if ((null != env.Headers) &&
                (env.Headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_ID)))
            {
                id = Guid.Parse(env.Headers[EnvelopeHeaderConstants.MESSAGE_ID]);
            }

            return id;
        }

        public static void SetMessageId(this Envelope env, Guid id)
        {
            if (null == env.Headers) { env.Headers = new Dictionary<string, string>(); }

            env.Headers[EnvelopeHeaderConstants.MESSAGE_ID] = id.ToString();
        }

        public static void SetMessageId(this Envelope env, string id)
        {
            // using un-safe Guid.Parse because I want invalid guid strings
            // to throw an exception
            env.SetMessageId(Guid.Parse(id));
        }

        public static Guid GetMessageId(this IDictionary<string, string> headers)
        {
            Guid id = Guid.Empty;

            if ((null != headers) &&
                (headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_ID)))
            {
                id = Guid.Parse(headers[EnvelopeHeaderConstants.MESSAGE_ID]);
            }

            return id;
        }

        public static void SetMessageId(this IDictionary<string, string> headers, Guid id)
        {
            if (null == headers) { headers = new Dictionary<string, string>(); }

            headers[EnvelopeHeaderConstants.MESSAGE_ID] = id.ToString();
        }

        public static void SetMessageId(this IDictionary<string, string> headers, string id)
        {
            // using un-safe Guid.Parse because I want invalid guid strings
            // to throw an exception
            headers.SetMessageId(Guid.Parse(id));
        }


        public static Guid GetCorrelationId(this Envelope env)
        {
            Guid id = Guid.Empty;

            if ((null != env.Headers) &&
                (env.Headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_CORRELATION_ID)))
            {
                id = Guid.Parse(env.Headers[EnvelopeHeaderConstants.MESSAGE_CORRELATION_ID]);
            }

            return id;
        }

        public static void SetCorrelationId(this Envelope env, Guid id)
        {
            if (null == env.Headers) { env.Headers = new Dictionary<string, string>(); }

            env.Headers[EnvelopeHeaderConstants.MESSAGE_CORRELATION_ID] = id.ToString();
        }

        public static void SetCorrelationId(this Envelope env, string id)
        {
            // using un-safe Guid.Parse because I want invalid guid strings
            // to throw an exception
            env.SetCorrelationId(Guid.Parse(id));
        }

        public static Guid GetCorrelationId(this IDictionary<string, string> headers)
        {
            Guid id = Guid.Empty;

            if ((null != headers) &&
                (headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_CORRELATION_ID)))
            {
                id = Guid.Parse(headers[EnvelopeHeaderConstants.MESSAGE_CORRELATION_ID]);
            }

            return id;
        }

        public static void SetCorrelationId(this IDictionary<string, string> headers, Guid id)
        {
            if (null == headers) { headers = new Dictionary<string, string>(); }

            headers[EnvelopeHeaderConstants.MESSAGE_CORRELATION_ID] = id.ToString();
        }

        public static void SetCorrelationId(this IDictionary<string, string> headers, string id)
        {
            // using un-safe Guid.Parse because I want invalid guid strings
            // to throw an exception
            headers.SetCorrelationId(Guid.Parse(id));
        }


        public static string GetMessageType(this Envelope env)
        {
            string msgType = null;

            if (env.Headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_TYPE))
            {
                msgType = env.Headers[EnvelopeHeaderConstants.MESSAGE_TYPE];
            }

            return msgType;
        }

        public static void SetMessageType(this Envelope env, string messageType)
        {
            env.Headers[EnvelopeHeaderConstants.MESSAGE_TYPE] = messageType;
        }

        public static string GetMessageType(this IDictionary<string, string> headers)
        {
            string msgType = null;

            if (headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_TYPE))
            {
                msgType = headers[EnvelopeHeaderConstants.MESSAGE_TYPE];
            }

            return msgType;
        }

        public static void SetMessageType(this IDictionary<string, string> headers, string messageType)
        {
            headers[EnvelopeHeaderConstants.MESSAGE_TYPE] = messageType;
        }


        public static string Flatten(this IDictionary<string, string> hash, string separator = ",")
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("[");
            hash.ToList().ForEach(kvp => sb.AppendFormat("{0}{{{1}:{2}}}", separator, kvp.Key, kvp.Value));

            if (sb.Length > 0)
            {
                sb.Remove(0, separator.Length);
            }

            sb.Append("]");
            return sb.ToString();
        }
    }
}

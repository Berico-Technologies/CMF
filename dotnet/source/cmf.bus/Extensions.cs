using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;

namespace cmf.bus
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


        public static string GetMessagePattern(this Envelope env)
        {
            string msgPattern = null;

            if (env.Headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_PATTERN))
            {
                msgPattern = env.Headers[EnvelopeHeaderConstants.MESSAGE_PATTERN];
            }

            return msgPattern;
        }

        public static void SetMessagePattern(this Envelope env, string pattern)
        {
            env.Headers[EnvelopeHeaderConstants.MESSAGE_PATTERN] = pattern;
        }

        public static string GetMessagePattern(this IDictionary<string, string> headers)
        {
            string msgPattern = null;

            if (headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_PATTERN))
            {
                msgPattern = headers[EnvelopeHeaderConstants.MESSAGE_PATTERN];
            }

            return msgPattern;
        }

        public static void SetMessagePattern(this IDictionary<string, string> headers, string pattern)
        {
            headers[EnvelopeHeaderConstants.MESSAGE_PATTERN] = pattern;
        }


        public static TimeSpan GetRpcTimeout(this Envelope env)
        {
            TimeSpan timeout = TimeSpan.Zero;

            if (env.Headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_PATTERN_RPC_TIMEOUT))
            {
                string rpcTimeoutString = env.Headers[EnvelopeHeaderConstants.MESSAGE_PATTERN_RPC_TIMEOUT];
                timeout = new TimeSpan(long.Parse(rpcTimeoutString));
            }

            return timeout;
        }

        public static void SetRpcTimeout(this Envelope env, TimeSpan timeout)
        {
            env.Headers[EnvelopeHeaderConstants.MESSAGE_PATTERN_RPC_TIMEOUT] = timeout.Ticks.ToString();
        }

        public static TimeSpan GetRpcTimeout(this IDictionary<string, string> headers)
        {
            TimeSpan timeout = TimeSpan.Zero;

            if (headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_PATTERN_RPC_TIMEOUT))
            {
                string rpcTimeoutString = headers[EnvelopeHeaderConstants.MESSAGE_PATTERN_RPC_TIMEOUT];
                timeout = new TimeSpan(long.Parse(rpcTimeoutString));
            }

            return timeout;
        }

        public static void SetRpcTimeout(this IDictionary<string, string> headers, TimeSpan timeout)
        {
            headers[EnvelopeHeaderConstants.MESSAGE_PATTERN_RPC_TIMEOUT] = timeout.Ticks.ToString();
        }


        public static string GetSenderIdentity(this Envelope env)
        {
            string identity = null;

            if (env.Headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_SENDER_IDENTITY))
            {
                identity = env.Headers[EnvelopeHeaderConstants.MESSAGE_SENDER_IDENTITY];
            }

            return identity;
        }

        public static void SetSenderIdentity(this Envelope env, string identity)
        {
            env.Headers[EnvelopeHeaderConstants.MESSAGE_SENDER_IDENTITY] = identity;
        }

        public static string GetSenderIdentity(this IDictionary<string, string> headers)
        {
            string identity = null;

            if (headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_SENDER_IDENTITY))
            {
                identity = headers[EnvelopeHeaderConstants.MESSAGE_SENDER_IDENTITY];
            }

            return identity;
        }

        public static void SetSenderIdentity(this IDictionary<string, string> headers, string identity)
        {
            headers[EnvelopeHeaderConstants.MESSAGE_SENDER_IDENTITY] = identity;
        }


        public static byte[] GetDigitalSignature(this Envelope env)
        {
            byte[] signature = new byte[0];

            if (env.Headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_SENDER_SIGNATURE))
            {
                string base64Signature = env.Headers[EnvelopeHeaderConstants.MESSAGE_SENDER_SIGNATURE];
                signature = Convert.FromBase64String(base64Signature);
            }

            return signature;
        }

        public static void SetDigitalSignature(this Envelope env, byte[] signature)
        {
            env.Headers[EnvelopeHeaderConstants.MESSAGE_SENDER_SIGNATURE] = Convert.ToBase64String(signature);
        }

        public static byte[] GetDigitalSignature(this IDictionary<string, string> headers)
        {
            byte[] signature = new byte[0];

            if (headers.ContainsKey(EnvelopeHeaderConstants.MESSAGE_SENDER_SIGNATURE))
            {
                string base64Signature = headers[EnvelopeHeaderConstants.MESSAGE_SENDER_SIGNATURE];
                signature = Convert.FromBase64String(base64Signature);
            }

            return signature;
        }

        public static void SetDigitalSignature(this IDictionary<string, string> headers, byte[] signature)
        {
            headers[EnvelopeHeaderConstants.MESSAGE_SENDER_SIGNATURE] = Convert.ToBase64String(signature);
        }


        public static DateTime GetCreationTime(this Envelope env)
        {
            string createTicks = null;

            if (env.Headers.ContainsKey(EnvelopeHeaderConstants.ENVELOPE_CREATION_TIME))
            {
                createTicks = env.Headers[EnvelopeHeaderConstants.ENVELOPE_CREATION_TIME];
            }

            return new DateTime(long.Parse(createTicks));
        }

        public static void SetCreationTime(this Envelope env, DateTime date)
        {
            env.Headers[EnvelopeHeaderConstants.ENVELOPE_CREATION_TIME] = date.Ticks.ToString();
        }


        public static DateTime GetReceiptTime(this Envelope env)
        {
            string receiptTicks = null;

            if (env.Headers.ContainsKey(EnvelopeHeaderConstants.ENVELOPE_RECEIPT_TIME))
            {
                receiptTicks = env.Headers[EnvelopeHeaderConstants.ENVELOPE_RECEIPT_TIME];
            }

            return new DateTime(long.Parse(receiptTicks));
        }

        public static void SetReceiptTime(this Envelope env, DateTime date)
        {
            env.Headers[EnvelopeHeaderConstants.ENVELOPE_RECEIPT_TIME] = date.Ticks.ToString();
        }

        public static DateTime GetReceiptTime(this IDictionary<string, string> headers)
        {
            string receiptTicks = null;

            if (headers.ContainsKey(EnvelopeHeaderConstants.ENVELOPE_RECEIPT_TIME))
            {
                receiptTicks = headers[EnvelopeHeaderConstants.ENVELOPE_RECEIPT_TIME];
            }

            return new DateTime(long.Parse(receiptTicks));
        }

        public static void SetReceiptTime(this IDictionary<string, string> headers, DateTime date)
        {
            headers[EnvelopeHeaderConstants.ENVELOPE_RECEIPT_TIME] = date.Ticks.ToString();
        }


        public static bool IsRpc(this Envelope env)
        {
            return string.Equals(EnvelopeHeaderConstants.MESSAGE_PATTERN_RPC, env.GetMessagePattern());
        }

        public static bool IsPubSub(this Envelope env)
        {
            return string.Equals(EnvelopeHeaderConstants.MESSAGE_PATTERN_PUBSUB, env.GetMessagePattern());
        }

        public static bool IsRequest(this Envelope env)
        {
            // we assume that the envelope is holding a request if it is marked
            // as an rpc message that has no correlation id set.
            Guid correlationId = env.GetCorrelationId();

            return ( (env.IsRpc()) && (Guid.Empty.Equals(correlationId)) );
        }


        public static string Flatten(this IDictionary<string, string> hash, string separator = ",")
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("[");
            hash.ToList().ForEach(kvp => sb.AppendFormat("{0}{{{1}:{2}}}", separator, kvp.Key, kvp.Value));

            if (sb.Length > 1)
            {
                sb.Remove(1, separator.Length);
            }

            sb.Append("]");
            return sb.ToString();
        }
    }
}

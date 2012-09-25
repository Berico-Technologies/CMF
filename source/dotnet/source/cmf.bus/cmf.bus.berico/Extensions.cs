﻿using System;
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

    }
}

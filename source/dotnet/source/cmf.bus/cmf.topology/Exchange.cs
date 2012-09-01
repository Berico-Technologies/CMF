using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.topology
{
    public struct Exchange
    {
        public string Name { get; private set; }
        public string HostName { get; private set; }
        public string VirtualHost { get; private set; }
        public int Port { get; private set; }
        public string RoutingKey { get; private set; }
        public string QueueName { get; private set; }
        public string ExchangeType { get; private set; }
        public bool IsDurable { get; private set; }
        public bool IsAutoDelete { get; private set; }
        public IDictionary Arguments { get; private set; }


        public Exchange(
            string name, 
            string hostName, 
            string vHost, 
            int port, 
            string routingKey, 
            string queueName = null, 
            string exchangeType = "topic", 
            bool isDurable = false, 
            bool autoDelete = false, 
            IDictionary arguments = null)
            : this()
        {
            this.Name = name;
            this.HostName = HostName;
            this.VirtualHost = vHost;
            this.Port = port;
            this.RoutingKey = routingKey;
            this.QueueName = queueName;
            this.ExchangeType = exchangeType;
            this.IsDurable = isDurable;
            this.IsAutoDelete = IsAutoDelete;
            this.Arguments = arguments;
        }
    }
}

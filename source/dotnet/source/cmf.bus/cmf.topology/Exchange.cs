using System;
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


        public Exchange(string name, string hostName, string vHost, int port, string routingKey, string queueName = null)
            : this()
        {
            this.Name = name;
            this.HostName = HostName;
            this.VirtualHost = vHost;
            this.Port = port;
            this.RoutingKey = routingKey;
            this.QueueName = queueName;
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus.berico;

namespace cmf.rabbit.topology
{
    public class SimpleTopologyService : ITopologyService
    {
        public string Name { get; protected set; }
        public string Hostname { get; protected set; }
        public string VirtualHost { get; protected set; }
        public int Port { get; protected set; }


        public SimpleTopologyService(string name = "", string hostname = "localhost", string vhost = "/", int port = 5672)
        {
            this.Name = name;
            this.Hostname = hostname;
            this.VirtualHost = vhost;
            this.Port = port;
        }


        public RoutingInfo GetRoutingInfo(IDictionary<string, string> routingHints)
        {
            Exchange theOneExchange = new Exchange(this.Name, this.Hostname, this.VirtualHost, this.Port, routingHints.GetMessageTopic(), string.Empty, "topic", false, true);
            RouteInfo theOneRoute = new RouteInfo(theOneExchange, theOneExchange);

            return new RoutingInfo(new RouteInfo[] { theOneRoute });
        }
    }
}

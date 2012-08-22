using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.rabbit
{
    public struct Exchange : IEquatable<Exchange>
    {
        public string HostName { get; set; }
        public string VirtualHost { get; set; }
        public int Port { get; set; }


        public Exchange(string hostName, string virtualHost, int port)
            : this()
        {
            this.HostName = hostName;
            this.VirtualHost = virtualHost;
            this.Port = port;
        }


        public override string ToString()
        {
            return string.Format("amqp://{0}:{1}{2}", this.HostName, this.Port, this.VirtualHost);
        }

        public override int GetHashCode()
        {
            return this.ToString().GetHashCode();
        }

        public override bool Equals(object obj)
        {
            bool isEqual = false;

            if (obj is Exchange)
            {
                isEqual = this.Equals((Exchange)obj);
            }

            return isEqual;
        }

        public bool Equals(Exchange other)
        {
            bool isEqual = false;

            if (this.GetHashCode() == other.GetHashCode())
            {
                isEqual = true;
            }

            return isEqual;
        }
    }
}

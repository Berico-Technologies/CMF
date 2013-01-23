using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RabbitMQ.Client;

using cmf.rabbit.topology;

namespace cmf.rabbit
{
    public interface IRabbitConnectionFactory : IDisposable
    {
        IConnection ConnectTo(Exchange exchange);
    }
}

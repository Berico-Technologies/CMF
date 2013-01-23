using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Common.Logging;
using RabbitMQ.Client;

using cmf.rabbit.topology;

namespace cmf.rabbit
{
    public class BasicConnectionFactory : IRabbitConnectionFactory
    {
        protected ILog _log;
        protected IDictionary<Exchange, IConnection> _connections;
        protected string _username;
        protected string _password;


        public BasicConnectionFactory(string username = "guest", string password = "guest")
        {
            _connections = new Dictionary<Exchange, IConnection>();
            _log = LogManager.GetLogger(this.GetType());

            _username = username;
            _password = password;
        }


        public IConnection ConnectTo(Exchange exchange)
        {
            IConnection connection = null;

            // first, see if we have a cached connection
            if (_connections.ContainsKey(exchange))
            {
                connection = _connections[exchange];

                if (!connection.IsOpen)
                {
                    _log.Info("Cached connection to RabbitMQ was closed: reconnecting");
                    connection = this.CreateConnection(exchange);
                }
            }
            else
            {
                _log.Debug("No connection to the exchange was cached: creating");
                connection = this.CreateConnection(exchange);

                // add the new connection to the cache
                _connections[exchange] = connection;
            }

            return connection;
        }

        public void Dispose()
        {
            foreach (IConnection conn in _connections.Values)
            {
                try { conn.Close(); }
                catch { }
            }
        }


        protected IConnection CreateConnection(Exchange exchange)
        {
            ConnectionFactory cf = new ConnectionFactory();
            cf.HostName = exchange.HostName;
            cf.VirtualHost = exchange.VirtualHost;
            cf.Port = exchange.Port;
            cf.UserName = _username;
            cf.Password = _password;

            return cf.CreateConnection();
        }
    }
}

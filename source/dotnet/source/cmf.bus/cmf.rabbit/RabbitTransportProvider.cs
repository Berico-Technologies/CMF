using System;
using System.Collections.Generic;
using System.DirectoryServices.AccountManagement;
using System.Linq;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using System.Text;

using RabbitMQ.Client;

using cmf.bus;
using cmf.bus.berico;
using cmf.security;
using cmf.topology;

namespace cmf.rabbit
{
    public class RabbitTransportProvider : ITransportProvider, IDisposable
    {
        public event Action<IEnvelopeDispatcher> OnEnvelopeReceived;


        protected IDictionary<IRegistration, RabbitListener> _listeners;
        protected IDictionary<Exchange, IConnection> _connections;
        protected ITopologyService _topoSvc;
        protected ICertificateProvider _certProvider;


        public RabbitTransportProvider(ITopologyService topologyService, ICertificateProvider certProvider)
        {
            _topoSvc = topologyService;
            _certProvider = certProvider;

            _listeners = new Dictionary<IRegistration, RabbitListener>();
        }


        public void Send(Envelope env)
        {
            throw new NotImplementedException();
        }

        public void Register(IRegistration registration)
        {
            // first, get the topology based on the registration info
            RoutingInfo routing = _topoSvc.GetRoutingInfo(registration.Info);

            // next, pull out all the consumer exchanges
            IEnumerable<Exchange> exchanges =
                from route in routing.Routes
                select route.ConsumerExchange;

            // next, get a connection to each of those exchanges
            IEnumerable<IConnection> connections = this.GetConnections(exchanges);

            // create and start a listener
            RabbitListener listener = new RabbitListener(registration, connections);
            listener.OnClose += _listeners.Remove;
            listener.Start();

            // store the listener
            _listeners.Add(registration, listener);
        }

        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }


        protected virtual void Dispose(bool disposing)
        {
            if (disposing)
            {
                // get rid of managed resources
                try { _listeners.Values.ToList().ForEach(l => l.Stop()); }
                catch { }

                try { _connections.Values.ToList().ForEach(c => c.Dispose()); }
                catch { }
            }
            // get rid of unmanaged resources
        }

        protected virtual IEnumerable<IConnection> GetConnections(IEnumerable<Exchange> exchanges)
        {
            List<IConnection> connections = new List<IConnection>();

            exchanges.ToList().ForEach(ex => connections.Add(this.GetConnection(ex)));

            return connections;
        }

        protected virtual IConnection GetConnection(Exchange ex)
        {
            IConnection conn = null;

            if (_connections.ContainsKey(ex))
            {
                conn = _connections[ex];
            }
            else
            {
                conn = this.CreateConnection(ex);
                _connections[ex] = conn;
            }

            return conn;
        }

        protected virtual IConnection CreateConnection(Exchange ex)
        {
            // use the cert provider to get the certificate to connect with
            X509Certificate2 cert = _certProvider.GetCertificate(ex.HostName, ex.Port);

            // we use the rabbit connection factory, just like normal
            ConnectionFactory cf = new ConnectionFactory();

            // set the hostname and the port
            cf.HostName = ex.HostName;
            cf.VirtualHost = ex.VirtualHost;
            cf.Port = ex.Port;

            // now, let's set the connection factory's ssl-specific settings
            // NOTE: it's absolutely required that what you set as Ssl.ServerName be
            //       what's on your rabbitmq server's certificate (its CN - common name)
            cf.Ssl.Certs = new X509CertificateCollection(new X509Certificate[] { cert });
            cf.Ssl.ServerName = ex.HostName;
            cf.Ssl.Enabled = true;

            // we're ready to create an SSL connection to the rabbitmq server
            return cf.CreateConnection();
        }
    }
}

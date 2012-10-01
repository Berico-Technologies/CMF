using System;
using System.Collections;
using System.Collections.Generic;
using System.DirectoryServices.AccountManagement;
using System.Linq;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.Threading;

using Common.Logging;
using RabbitMQ.Client;

using cmf.bus;
using cmf.bus.berico;
using cmf.rabbit.security;
using cmf.rabbit.topology;

namespace cmf.rabbit
{
    public class RabbitTransportProvider : ITransportProvider, IDisposable
    {
        public event Action<IEnvelopeDispatcher> OnEnvelopeReceived;


        protected IDictionary<IRegistration, RabbitListener> _listeners;
        protected IDictionary<Exchange, IConnection> _connections;
        protected ITopologyService _topoSvc;
        protected ICertificateProvider _certProvider;
        protected ILog _log;


        public RabbitTransportProvider(ITopologyService topologyService, ICertificateProvider certProvider)
        {
            _topoSvc = topologyService;
            _certProvider = certProvider;

            _connections = new Dictionary<Exchange, IConnection>();
            _listeners = new Dictionary<IRegistration, RabbitListener>();

            _log = LogManager.GetLogger(this.GetType());
        }


        public void Send(Envelope env)
        {
            _log.Debug("Enter Send");

            // first, get the topology based on the headers
            RoutingInfo routing = _topoSvc.GetRoutingInfo(env.Headers);

            // next, pull out all the producer exchanges
            IEnumerable<Exchange> exchanges =
                from route in routing.Routes
                select route.ProducerExchange;

            // for each exchange, send the envelope
            foreach (Exchange ex in exchanges)
            {
                _log.Debug("Sending to exchange: " + ex.ToString());
                IConnection conn = this.GetConnection(ex);

                using (IModel channel = conn.CreateModel())
                {
                    IBasicProperties props = channel.CreateBasicProperties();
                    props.Headers = env.Headers as IDictionary;

                    channel.ExchangeDeclare(ex.Name, ex.ExchangeType, ex.IsDurable, ex.IsAutoDelete, ex.Arguments);
                    channel.BasicPublish(ex.Name, ex.RoutingKey, props, env.Payload);
                }
            }

            _log.Debug("Leave Send");
        }

        public void Register(IRegistration registration)
        {
            _log.Debug("Enter Register");

            // first, get the topology based on the registration info
            RoutingInfo routing = _topoSvc.GetRoutingInfo(registration.Info);

            // next, pull out all the consumer exchanges
            IEnumerable<Exchange> exchanges =
                from route in routing.Routes
                select route.ConsumerExchange;

            foreach (Exchange ex in exchanges)
            {
                IConnection conn = this.GetConnection(ex);

                // create a listener
                RabbitListener listener = new RabbitListener(registration, ex, conn);
                listener.OnEnvelopeReceived += this.listener_OnEnvelopeReceived;
                listener.OnClose += _listeners.Remove;

                // put it on another thread so as not to block this one
                Thread listenerThread = new Thread(listener.Start);
                listenerThread.Name = string.Format("{0} on {1}:{2}{3}", ex.QueueName, ex.HostName, ex.Port, ex.VirtualHost);
                listenerThread.Start();

                // store the listener
                _listeners.Add(registration, listener);
            }

            _log.Debug("Leave Register");
        }

        public virtual void Unregister(IRegistration registration)
        {
            if (_listeners.ContainsKey(registration))
            {
                RabbitListener listener = _listeners[registration];
                listener.Stop();

                _listeners.Remove(registration);
            }
        }

        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }


        protected virtual void listener_OnEnvelopeReceived(IEnvelopeDispatcher dispatcher)
        {
            if (null != this.OnEnvelopeReceived)
            {
                foreach (Delegate d in this.OnEnvelopeReceived.GetInvocationList())
                {
                    try { d.DynamicInvoke(dispatcher); }
                    catch { }
                }
            }
        }

        protected virtual void Dispose(bool disposing)
        {
            _log.Debug("Enter Dispose");

            if (disposing)
            {
                // get rid of managed resources
                try { _listeners.Values.ToList().ForEach(l => l.Stop()); }
                catch { }

                try { _connections.Values.ToList().ForEach(c => c.Dispose()); }
                catch { }
            }
            // get rid of unmanaged resources

            _log.Debug("Leave Dispose");
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
            _log.Debug("Enter CreateConnection");

            IConnection conn = null;

            // use the cert provider to get the certificate to connect with
            X509Certificate2 cert = _certProvider.GetCertificate();

            // we use the rabbit connection factory, just like normal
            ConnectionFactory cf = new ConnectionFactory();

            // set the hostname and the port
            cf.HostName = ex.HostName;
            cf.VirtualHost = ex.VirtualHost;
            cf.Port = ex.Port;

            if (null != cert)
            {
                _log.Info("A certificate was located with subject: " + cert.Subject);

                // now, let's set the connection factory's ssl-specific settings
                // NOTE: it's absolutely required that what you set as Ssl.ServerName be
                //       what's on your rabbitmq server's certificate (its CN - common name)
                cf.AuthMechanisms = new AuthMechanismFactory[] { new ExternalMechanismFactory() };
                cf.Ssl.Certs = new X509CertificateCollection(new X509Certificate[] { cert });
                cf.Ssl.ServerName = ex.HostName;
                cf.Ssl.Enabled = true;
            }

            // we either now create an SSL connection or a default "guest/guest" connection
            conn = cf.CreateConnection();

            _log.Debug("Leave CreateConnection");
            return conn;
        }
    }
}

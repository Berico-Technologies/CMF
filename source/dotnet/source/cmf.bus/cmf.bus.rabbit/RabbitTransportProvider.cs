using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography.X509Certificates;
using System.Text;

using Common.Logging;
using RabbitMQ.Client;

using cmf.bus.core;
using System.Security.Cryptography;
using System.DirectoryServices.AccountManagement;

namespace cmf.bus.rabbit
{
    public class RabbitTransportProvider : ITransportProvider
    {
        public event Action<Envelope> OnEnvelopeReceived;


        private ITopologyService _topoSvc;
        private IDictionary<string, Tuple<Exchange, IConnection>> _connectedExchanges;
        private X509Certificate2 _cert;
        private ILog _log;


        public RabbitTransportProvider(ITopologyService topologyService)
        {
            _topoSvc = topologyService;
            _connectedExchanges = new Dictionary<string, Tuple<Exchange, IConnection>>();
            _log = LogManager.GetLogger(this.GetType());
        }


        public void Send(Envelope env)
        {
            Exchange ex = _topoSvc.GetExchangeForTopic(env.GetMessageTopic());
            this.EnsureConnectionTo(env.GetMessageTopic(), ex);
        }

        public void Register(string topic)
        {
            Exchange ex = _topoSvc.GetExchangeForTopic(topic);
            this.EnsureConnectionTo(topic, ex);


        }

        public void Initialize()
        {
            // I've imported my certificate into my certificate store 
            // (the Personal/Certificates folder in the certmgr mmc snap-in)
            // Let's open that store right now.
            X509Store certStore = new X509Store(StoreName.My, StoreLocation.CurrentUser);
            certStore.Open(OpenFlags.ReadOnly);

            // the DN I get is CN=name,CN=Users,DC=example,DC=com
            // but the DN on the cert has spaces after each comma
            string spacedDN = UserPrincipal.Current.DistinguishedName.Replace(",", ", ");

            // get and store the certificate
            _cert = certStore.Certificates
                .Find(
                    X509FindType.FindBySubjectDistinguishedName,
                    spacedDN,
                    true)
                .OfType<X509Certificate2>()
                .FirstOrDefault();
        }
        
        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }


        protected virtual void EnsureConnectionTo(string topic, Exchange ex)
        {
            if (false == _connectedExchanges.ContainsKey(topic))
            {
                // we use the rabbit connection factory, just like normal
                ConnectionFactory cf = new ConnectionFactory();

                // set the hostname and the port
                cf.HostName = ex.HostName;
                cf.VirtualHost = ex.VirtualHost;
                cf.Port = ex.Port;

                // now, let's set the connection factory's ssl-specific settings
                // NOTE: it's absolutely required that what you set as Ssl.ServerName be
                //       what's on your rabbitmq server's certificate (its CN - common name)
                cf.Ssl.Certs = new X509CertificateCollection(new X509Certificate[] { _cert });
                cf.Ssl.ServerName = ex.HostName;
                cf.Ssl.Enabled = true;

                // we're ready to create an SSL connection to the rabbitmq server
                using (IConnection conn = cf.CreateConnection())
                {
                    using (IModel channel = conn.CreateModel())
                    {
                    }
                }
            }
        }

        protected virtual void Dispose(bool disposing)
        {
            if (disposing)
            {
                foreach (Tuple<Exchange, IConnection> connInfo in _connectedExchanges.Values)
                {
                    using (connInfo.Item2)
                    {
                        connInfo.Item2.Close();
                    }
                }
            }

            // get rid of unmanaged resources here, if any
        }


        private byte[] SignData(byte[] data, X509Certificate2 cert)
        {
            RSACryptoServiceProvider rsaProvider = cert.PrivateKey as RSACryptoServiceProvider;
            return rsaProvider.SignData(data, new SHA1CryptoServiceProvider());
        }
    }
}

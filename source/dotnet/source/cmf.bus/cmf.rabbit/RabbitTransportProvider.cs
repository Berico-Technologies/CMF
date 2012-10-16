using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

using Common.Logging;
using RabbitMQ.Client;

using cmf.bus;
using cmf.bus.berico;
using cmf.rabbit.topology;

namespace cmf.rabbit
{
    public class RabbitTransportProvider : ITransportProvider, IDisposable
    {
        public event Action<IEnvelopeDispatcher> OnEnvelopeReceived;


        protected IDictionary<IRegistration, RabbitListener> _listeners;
        protected ITopologyService _topoSvc;
        protected IRabbitConnectionFactory _connFactory;
        protected ILog _log;


        public RabbitTransportProvider(
            ITopologyService topologyService, 
            IRabbitConnectionFactory connFactory)
        {
            _topoSvc = topologyService;
            _connFactory = connFactory;

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
                IConnection conn = _connFactory.ConnectTo(ex);
                
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
                IConnection conn = _connFactory.ConnectTo(ex);

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
            }
            // get rid of unmanaged resources

            _log.Debug("Leave Dispose");
        }
    }
}

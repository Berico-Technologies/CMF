using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using log4net;

using cmf.bus.core;
using cmf.bus.topology;
using cmf.bus.transport;

namespace cmf.bus.rabbit
{
    public class RabbitTransporter : IInboundTransporter, IOutboundTransporter
    {
        public event Action<Envelope> EnvelopeReceived;


        private static ILog LOG = LogManager.GetLogger(typeof(RabbitTransporter));

        private ITopologyService _topoSvc;
        private IDictionary<string, RabbitExchange> _connectedExchanges;


        public RabbitTransporter(ITopologyService topologyService)
        {
            _topoSvc = topologyService;
            _connectedExchanges = new Dictionary<string, RabbitExchange>();
        }


        public void RegisterFor(string topic)
        {
            if (string.IsNullOrEmpty(topic))
            {
                throw new ArgumentNullException("Cannot register on a null (or empty) topic");
            }

            
        }

        public void Send(Envelope envelope)
        {
            throw new NotImplementedException();
        }
    }
}

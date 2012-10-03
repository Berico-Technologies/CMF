using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

using Common.Logging;

using cmf.bus;
using cmf.bus.berico;

namespace cmf.eventing.berico
{
    public class RpcRegistration : IRegistration
    {
        protected IEnumerable<IInboundEventProcessor> _inboundChain;
        protected Predicate<Envelope> _responseFilter;
        protected AutoResetEvent _responseEvent;
        protected ILog _log;
        protected Envelope _responseEnvelope;


        public Predicate<Envelope> Filter
        {
            get { return _responseFilter; }
        }

        public IDictionary<string, string> Info { get; protected set; }
        
        
        public RpcRegistration(Guid requestId, string expectedTopic, IEnumerable<IInboundEventProcessor> chain)
        {
            _inboundChain = chain;

            _responseEvent = new AutoResetEvent(false);
            _responseFilter = env => Guid.Equals(env.GetCorrelationId(), requestId);
            
            this.Info = new Dictionary<string, string>();
            this.Info.SetMessageTopic(this.BuildRpcTopic(expectedTopic, requestId));

            _log = LogManager.GetLogger(this.GetType());
        }


        public virtual object Handle(Envelope env)
        {
            _responseEnvelope = env;
            _responseEvent.Set();

            return null;
        }

        public virtual object HandleFailed(Envelope env, Exception ex)
        {
            _log.Error("Failed to handle an envelope: " + env.Headers.Flatten(), ex);

            return null;
        }

        public virtual object GetResponse(TimeSpan timeout)
        {
            object response = null;

            if (_responseEvent.WaitOne(timeout))
            {
                try
                {
                    this.ProcessInbound(ref response, ref _responseEnvelope);
                }
                catch (Exception ex)
                {
                    this.HandleFailed(_responseEnvelope, ex);
                }
            }

            return response;
        }


        protected virtual void ProcessInbound(ref object ev, ref Envelope env)
        {
            IDictionary<string, object> processorContext = new Dictionary<string, object>();

            foreach (IInboundEventProcessor processor in _inboundChain)
            {
                processor.ProcessInbound(ref ev, ref env, ref processorContext);
            }
        }

        protected virtual string BuildRpcTopic(string expectedTopic, Guid requestId)
        {
            return string.Format("{0}#{1}", expectedTopic, requestId.ToString());
        }
    }
}

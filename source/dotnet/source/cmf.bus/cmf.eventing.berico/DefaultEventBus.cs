using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Common.Logging;

using cmf.bus;
using cmf.bus.berico;
using cmf.eventing;

namespace cmf.eventing.berico
{
    public class DefaultEventBus : IEventBus
    {
        protected DefaultEnvelopeBus _envBus;
        protected ILog _log;


        public IDictionary<int, IInboundEventProcessor> InboundChain { get; set; }
        public IDictionary<int, IOutboundEventProcessor> OutboundChain { get; set; }


        public DefaultEventBus(DefaultEnvelopeBus envBus)
        {
            _envBus = envBus;
            _log = LogManager.GetLogger(this.GetType());
        }


        public virtual void Publish(object ev)
        {
            _log.Debug("Enter Publish");

            if (null == ev) { throw new ArgumentNullException("Cannot publish a null event"); }

            try
            {
                Envelope env = new Envelope();
                this.ProcessOutbound(ev, env);
                _envBus.Send(env);
            }
            catch (Exception ex)
            {
                _log.Error("Exception publishing an event", ex);
                throw;
            }

            _log.Debug("Leave Publish");
        }

        public virtual void Subscribe(IEventHandler handler)
        {
            _log.Debug("Enter Subscribe");

            EventRegistration registration = new EventRegistration(handler, this.InboundChain.Sort());
            _envBus.Register(registration);

            _log.Debug("Leave Subscribe");
        }

        public virtual void Subscribe<TEvent>(Action<TEvent, IDictionary<string, string>> handler) where TEvent : class
        {
            this.Subscribe(new TypedEventHandler<TEvent>(handler));
        }

        public virtual void Dispose()
        {
            _log.Info("The event bus client will now be disposed");
            _envBus.Dispose();
        }


        protected virtual void ProcessOutbound(object ev, Envelope env)
        {
            IDictionary<string, object> processorContext = new Dictionary<string, object>();

            foreach (IOutboundEventProcessor processor in this.OutboundChain.Sort())
            {
                processor.ProcessOutbound(ref ev, ref env, processorContext);
            }
        }
    }




    public static class ChainExtensions
    {
        public static IEnumerable<IOutboundEventProcessor> Sort(this IDictionary<int, IOutboundEventProcessor> chain)
        {
            IEnumerable<IOutboundEventProcessor> sortedChain = new List<IOutboundEventProcessor>();

            if (null != chain)
            {
                sortedChain = chain
                    .OrderBy(kvp => kvp.Key)
                    .Select<KeyValuePair<int, IOutboundEventProcessor>, IOutboundEventProcessor>(kvp => kvp.Value);
            }

            return sortedChain;
        }

        public static IEnumerable<IInboundEventProcessor> Sort(this IDictionary<int, IInboundEventProcessor> chain)
        {
            IEnumerable<IInboundEventProcessor> sortedChain = new List<IInboundEventProcessor>();

            if (null != chain)
            {
                sortedChain = chain
                    .OrderBy(kvp => kvp.Key)
                    .Select<KeyValuePair<int, IInboundEventProcessor>, IInboundEventProcessor>(kvp => kvp.Value);
            }

            return sortedChain;
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;
using cmf.bus.berico;
using cmf.eventing;

namespace cmf.eventing.berico
{
    public class DefaultEventBus : IEventBus
    {
        protected DefaultEnvelopeBus _envBus;
        protected SortedDictionary<int, IInboundEventProcessor> _inboundChain;
        protected SortedDictionary<int, IOutboundEventProcessor> _outboundChain;


        public DefaultEventBus(
            DefaultEnvelopeBus envBus,
            IDictionary<int, IInboundEventProcessor> inboundProcessorChain,
            IDictionary<int, IOutboundEventProcessor> outboundProcessorChain)
        {
            _envBus = envBus;
            _inboundChain = new SortedDictionary<int, IInboundEventProcessor>(inboundProcessorChain);
            _outboundChain = new SortedDictionary<int, IOutboundEventProcessor>(outboundProcessorChain);
        }


        public void Publish(object ev)
        {
            if (null == ev) { throw new ArgumentNullException("Cannot publish a null event"); }

            Envelope env = new Envelope();

            this.ProcessOutbound(ev, env);

            _envBus.Send(env);
        }

        public void Subscribe(IEventHandler handler)
        {
            EventRegistration registration = new EventRegistration(handler, _inboundChain);

            _envBus.Register(registration);
        }

        public void Subscribe<TEvent>(Action<TEvent, IDictionary<string, string>> handler) where TEvent : class
        {
            this.Subscribe(new TypedEventHandler<TEvent>(handler));
        }


        protected virtual void ProcessOutbound(object ev, Envelope env)
        {
            IDictionary<string, object> processorContext = new Dictionary<string, object>();

            foreach (IOutboundEventProcessor processor in _outboundChain.Values)
            {
                processor.ProcessOutbound(ref ev, ref env, processorContext);
            }
        }
    }
}

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


        public IDictionary<int, IInboundEventProcessor> InboundChain { get; set; }
        public IDictionary<int, IOutboundEventProcessor> OutboundChain { get; set; }


        public DefaultEventBus(DefaultEnvelopeBus envBus)
        {
            _envBus = envBus;
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
            EventRegistration registration = new EventRegistration(handler, this.InboundChain.Sort());

            _envBus.Register(registration);
        }

        public void Subscribe<TEvent>(Action<TEvent, IDictionary<string, string>> handler) where TEvent : class
        {
            this.Subscribe(new TypedEventHandler<TEvent>(handler));
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
            return chain
                .OrderBy(kvp => kvp.Key)
                .Select<KeyValuePair<int, IOutboundEventProcessor>, IOutboundEventProcessor>(kvp => kvp.Value);
        }

        public static IEnumerable<IInboundEventProcessor> Sort(this IDictionary<int, IInboundEventProcessor> chain)
        {
            return chain
                .OrderBy(kvp => kvp.Key)
                .Select<KeyValuePair<int, IInboundEventProcessor>, IInboundEventProcessor>(kvp => kvp.Value);
        }
    }
}

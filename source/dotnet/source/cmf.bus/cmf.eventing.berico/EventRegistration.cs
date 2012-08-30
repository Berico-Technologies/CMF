using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;

namespace cmf.eventing.berico
{
    public class EventRegistration : IRegistration
    {
        protected IEventHandler _handler;
        protected IDictionary<string, string> _registrationInfo;
        protected SortedDictionary<int, IInboundEventProcessor> _inboundChain;


        public virtual Predicate<Envelope> Filter
        {
            get { return null; }
        }

        public virtual IDictionary<string, string> Info
        {
            get { return _registrationInfo; }
        }


        public EventRegistration(IEventHandler handler, SortedDictionary<int, IInboundEventProcessor> inboundChain)
        {
            _handler = handler;
            _inboundChain = inboundChain;

            _registrationInfo = new Dictionary<string, string>();
            _registrationInfo.Add("topic", handler.Topic);
        }


        public virtual object Handle(Envelope env)
        {
            object ev = null;
            object result = null;

            try
            {
                this.ProcessInbound(ref ev, ref env);
                result = _handler.Handle(ev, env.Headers);
            }
            catch (Exception ex)
            {
                result = this.HandleFailed(env, ex);
            }

            return result;
        }

        public virtual object HandleFailed(Envelope env, Exception ex)
        {
            return _handler.HandleFailed(env, ex);
        }


        protected virtual void ProcessInbound(ref object ev, ref Envelope env)
        {
            IDictionary<string, object> processorContext = new Dictionary<string, object>();

            foreach (IInboundEventProcessor processor in _inboundChain.Values)
            {
                processor.ProcessInbound(ref ev, ref env, ref processorContext);
            }
        }
    }
}

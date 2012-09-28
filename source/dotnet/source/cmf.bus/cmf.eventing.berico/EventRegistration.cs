﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Common.Logging;

using cmf.bus;
using cmf.bus.berico;

namespace cmf.eventing.berico
{
    public class EventRegistration : IRegistration
    {
        protected IEventHandler _handler;
        protected IDictionary<string, string> _registrationInfo;
        protected IEnumerable<IInboundEventProcessor> _inboundChain;
        protected ILog _log;


        public virtual Predicate<Envelope> Filter
        {
            get { return null; }
        }

        public virtual IDictionary<string, string> Info
        {
            get { return _registrationInfo; }
        }


        public EventRegistration(IEventHandler handler, IEnumerable<IInboundEventProcessor> inboundChain)
        {
            _handler = handler;
            _inboundChain = inboundChain;

            _registrationInfo = new Dictionary<string, string>();
            _registrationInfo.SetMessageTopic(handler.Topic);

            _log = LogManager.GetLogger(this.GetType());
        }


        public virtual object Handle(Envelope env)
        {
            _log.Debug("Enter Handle");

            object ev = null;
            object result = null;

            try
            {
                this.ProcessInbound(ref ev, ref env);
                result = _handler.Handle(ev, env.Headers);
            }
            catch (Exception ex)
            {
                _log.Warn("Caught an exception while handling an event", ex);
                result = this.HandleFailed(env, ex);
            }

            _log.Debug("Leave Handle");
            return result;
        }

        public virtual object HandleFailed(Envelope env, Exception ex)
        {
            _log.Debug("Enter HandleFailed");

            // either log & return or log & throw
            try
            {
                _log.Debug("Leave HandleFailed");
                return _handler.HandleFailed(env, ex);
            }
            catch (Exception failedToFail)
            {
                _log.Error("Caught an exception attempting to raise the Failed event", failedToFail);
                throw;
            }
        }


        protected virtual void ProcessInbound(ref object ev, ref Envelope env)
        {
            IDictionary<string, object> processorContext = new Dictionary<string, object>();

            foreach (IInboundEventProcessor processor in _inboundChain)
            {
                processor.ProcessInbound(ref ev, ref env, ref processorContext);
            }
        }
    }
}

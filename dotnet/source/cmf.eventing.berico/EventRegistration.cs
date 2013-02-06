using System;
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
        protected Func<IEventHandler, Envelope, object> _interceptor;
        protected IDictionary<string, string> _registrationInfo;
        protected ILog _log;


        public virtual Predicate<Envelope> Filter
        {
            get { return null; }
        }

        public virtual IDictionary<string, string> Info
        {
            get { return _registrationInfo; }
        }

        public virtual IEventHandler Handler { get; protected set; }
        
        public virtual Envelope Envelope { get; protected set; }


        public EventRegistration(
            IEventHandler handler, 
            Func<IEventHandler, Envelope, object> interceptor)
        {
            this.Handler = handler;
            _interceptor = interceptor;

            _registrationInfo = new Dictionary<string, string>();
            _registrationInfo.SetMessageTopic(handler.Topic);

            _log = LogManager.GetLogger(this.GetType());
        }


        public virtual object Handle(Envelope env)
        {
            _log.Debug("Inside RegistrationInterceptor.Handle(Envelope env)");

            return _interceptor(this.Handler, env);
        }

        public virtual object HandleFailed(Envelope env, Exception ex)
        {
            _log.Debug("Enter HandleFailed");

            // either succeed & return the result or fail & return null
            try
            {
                _log.Debug("Leave HandleFailed");
                return this.Handler.HandleFailed(env, ex);
            }
            catch (Exception failedToFail)
            {
                _log.Error("Caught an exception attempting to raise the Failed event", failedToFail);
                return null;
            }
        }
    }
}

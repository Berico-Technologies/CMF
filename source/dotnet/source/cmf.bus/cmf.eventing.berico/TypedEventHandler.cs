using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Common.Logging;

using cmf.bus;
using cmf.bus.berico;

namespace cmf.eventing.berico
{
    public class TypedEventHandler<TEvent> : IEventHandler where TEvent : class
    {
        protected Func<TEvent, IDictionary<string, string>, object> _handler;
        protected Func<Envelope, Exception, object> _failHandler;
        protected ILog _log;


        public string Topic
        {
            get { return typeof(TEvent).FullName; }
        }


        public TypedEventHandler(Action<TEvent, IDictionary<string, string>> noReturnHandler)
        {
            _handler = new Func<TEvent,IDictionary<string,string>,object>(delegate (TEvent ev, IDictionary<string, string> headers) {
                noReturnHandler(ev, headers);
                return null;
            });

            _log = LogManager.GetLogger(this.GetType());
        }

        public TypedEventHandler(Func<TEvent, IDictionary<string, string>, object> handler)
        {
            _handler = handler;

            _log = LogManager.GetLogger(this.GetType());
        }

        public TypedEventHandler(
            Func<TEvent, IDictionary<string, string>, object> handler,
            Func<Envelope, Exception, object> failHandler)
            : this(handler)
        {
            _failHandler = failHandler;
        }


        public object Handle(object ev, IDictionary<string, string> headers)
        {
            if (null == ev)
            {
                throw new ArgumentNullException("Cannot handle a null event");
            }

            _log.Debug(string.Format("Raising event of type {0} to consumer (requesting {1}) with headers {2}", 
                ev.GetType().FullName, this.Topic, headers.Flatten()));

            return _handler(ev as TEvent, headers);
        }

        public object HandleFailed(Envelope env, Exception ex)
        {
            object result = null;

            if (null != _failHandler) { result = _failHandler(env, ex); }

            return result;
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;

namespace cmf.eventing.berico
{
    public class TypedEventHandler<TEvent> : IEventHandler where TEvent : class
    {
        protected Func<TEvent, IDictionary<string, string>, object> _handler;
        protected Func<Envelope, Exception, object> _failHandler;


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
        }

        public TypedEventHandler(Func<TEvent, IDictionary<string, string>, object> handler)
        {
            _handler = handler;
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

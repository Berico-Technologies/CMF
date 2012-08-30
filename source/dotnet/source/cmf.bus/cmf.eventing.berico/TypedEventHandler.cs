using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;

namespace cmf.eventing.berico
{
    public class TypedEventHandler<TEvent> : IEventHandler where TEvent : class
    {
        protected Action<TEvent, IDictionary<string, string>> _handler;
        protected Action<Envelope, Exception> _failHandler;


        public string Topic
        {
            get { return typeof(TEvent).FullName; }
        }


        public TypedEventHandler(Action<TEvent, IDictionary<string, string>> handler)
        {
            _handler = handler;
        }

        public TypedEventHandler(
            Action<TEvent, IDictionary<string, string>> handler,
            Action<Envelope, Exception> failHandler)
            : this(handler)
        {
            _failHandler = failHandler;
        }


        public void Handle(object ev, IDictionary<string, string> headers)
        {
            _handler(ev as TEvent, headers);
        }

        public void HandleFailed(Envelope env, Exception ex)
        {
            if (null != _failHandler) { _failHandler(env, ex); }
        }
    }
}

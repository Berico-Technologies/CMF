using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus.core;

namespace cmf.bus.eventing.core
{
    public class TypedEventHandler<TEvent> : IEventHandler where TEvent : class
    {
        private Func<TEvent, DeliveryOutcome> _typedHandler;


        public string Topic
        {
            get { return typeof(TEvent).FullName; }
            set { }
        }


        public TypedEventHandler(Func<TEvent, DeliveryOutcome> typedHandler)
        {
            _typedHandler = typedHandler;
        }


        public DeliveryOutcome Handle(object ev)
        {
            return _typedHandler(ev as TEvent);
        }
    }
}

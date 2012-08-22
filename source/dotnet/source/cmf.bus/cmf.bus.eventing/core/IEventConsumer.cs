using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus.core;

namespace cmf.bus.eventing.core
{
    public interface IEventConsumer
    {
        event Action<Envelope, Exception> OnFailedEnvelope;


        void Subscribe(IEventHandler handler);

        void Subscribe<TEvent>(Func<TEvent, DeliveryOutcome> handler) where TEvent : class;
    }
}

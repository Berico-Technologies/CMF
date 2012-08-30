using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;

namespace cmf.eventing
{
    public interface IEventConsumer
    {
        void Subscribe(IEventHandler handler);

        void Subscribe<TEvent>(Action<TEvent, IDictionary<string, string>> handler) where TEvent : class;
    }
}

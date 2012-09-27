using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing
{
    public interface IEventBus : IEventProducer, IEventConsumer, IDisposable
    {
        
    }
}

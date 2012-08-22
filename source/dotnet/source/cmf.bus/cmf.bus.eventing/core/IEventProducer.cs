using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.eventing.core
{
    public interface IEventProducer
    {
        void Publish(object ev);
    }
}

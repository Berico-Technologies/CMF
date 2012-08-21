using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus.core;

namespace cmf.bus.eventing.core
{
    public interface IEventHandler
    {
        string Topic { get; set; }

        DeliveryOutcome Handle(object ev);
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.core
{
    public interface IRegistration
    {
        string Topic { get; set; }

        Predicate<Envelope> TransportFilter { get; set; }

        DeliveryOutcome Handle(Envelope env);
    }
}

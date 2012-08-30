using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.rabbit
{
    public enum DeliveryOutcomes
    {
        Null,
        Acknowledge,
        Reject,
        Exception
    }
}

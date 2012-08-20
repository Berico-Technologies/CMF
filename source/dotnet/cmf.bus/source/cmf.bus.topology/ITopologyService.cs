using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.topology
{
    public interface ITopologyService
    {
        IExchange GetExchangeForTopic(string topic);
    }
}

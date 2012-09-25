using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.rabbit.topology
{
    public interface ITopologyService
    {
        RoutingInfo GetRoutingInfo(IDictionary<string, string> routingHints);
    }
}

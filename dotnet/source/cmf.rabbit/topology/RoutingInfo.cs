using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.rabbit.topology
{
    public struct RoutingInfo
    {
        public IEnumerable<RouteInfo> Routes { get; private set; }


        public RoutingInfo(IEnumerable<RouteInfo> routes)
            : this()
        {
            this.Routes = routes;
        }
    }
}

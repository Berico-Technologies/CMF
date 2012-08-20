using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.topology
{
    public interface IExchange
    {
        string Hostname { get; set; }
        string VirtualHost { get; set; }
        string Name { get; set; }
    }
}

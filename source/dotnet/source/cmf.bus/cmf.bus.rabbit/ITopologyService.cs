using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.rabbit
{
    //TODO: discover the real home of this interface.  It doesn't belong here.
    public interface ITopologyService
    {
        Exchange GetExchangeForTopic(string topic);
    }
}

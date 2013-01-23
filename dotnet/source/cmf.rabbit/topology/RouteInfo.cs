using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.rabbit.topology
{
    public struct RouteInfo
    {
        public Exchange ProducerExchange { get; private set; }
        public Exchange ConsumerExchange { get; private set; }


        public RouteInfo(Exchange producerExchange, Exchange consumerExchange)
             : this()
        {
            this.ProducerExchange = producerExchange;
            this.ConsumerExchange = consumerExchange;
        }
    }
}

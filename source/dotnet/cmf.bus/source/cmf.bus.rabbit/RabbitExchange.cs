using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus.topology;

namespace cmf.bus.rabbit
{
    public class RabbitExchange : IExchange
    {
        private IExchange _baseExchange;


        public string Hostname { get; set; }
        public string VirtualHost { get; set; }
        public string Name { get; set; }


        public RabbitExchange(IExchange baseExchange)
        {
            _baseExchange = baseExchange;
        }
    }
}

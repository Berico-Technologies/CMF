using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.core
{
    public class TransportFilteredRegistration : IRegistration
    {
        public string Topic
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }


        public TransportFilteredRegistration(Predicate<Envelope> transportFilter)
        {
        }

        public DeliveryOutcome Handle(Envelope env)
        {
            throw new NotImplementedException();
        }
    }
}

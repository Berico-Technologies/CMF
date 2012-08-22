using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus.core
{
    public class FunctionalRegistration : IRegistration
    {
        private Func<Envelope, DeliveryOutcome> _handlerFunction;


        public string Topic { get; set; }

        public Predicate<Envelope> TransportFilter { get; set; }


        public FunctionalRegistration(string topic, Func<Envelope, DeliveryOutcome> handlerFunction)
        {
            this.Topic = topic;
            _handlerFunction = handlerFunction;
        }

        public FunctionalRegistration(string topic, Func<Envelope, DeliveryOutcome> handlerFunction, Predicate<Envelope> transportFilter)
            : this(topic, handlerFunction)
        {
            this.TransportFilter = transportFilter;
        }


        public DeliveryOutcome Handle(Envelope env)
        {
            return _handlerFunction(env);
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Spring.Core;
using Spring.Context.Support;

using cmf.bus.core;


namespace cmf.example.client
{
    class Program
    {
        private IEnvelopeBus _envBus;


        static void Main(string[] args)
        {
        }


        public Program(IEnvelopeBus envBus)
        {
            _envBus = envBus;
        }


        public void Start()
        {
            this.RegisterForEnvelopes();
        }


        protected void RegisterForEnvelopes()
        {
            _envBus.Register(new FunctionalRegistration("mil.capture.disposition.UserDispositionEvent", this.Handle_UserDispositionEvent));
        }


        private DeliveryOutcome Handle_UserDispositionEvent(Envelope env)
        {
            return DeliveryOutcome.Acknowledge;
        }
    }
}

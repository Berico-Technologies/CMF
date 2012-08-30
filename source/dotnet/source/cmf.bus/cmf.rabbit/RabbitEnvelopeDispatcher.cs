using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;
using cmf.bus.berico;

namespace cmf.rabbit
{
    public class RabbitEnvelopeDispatcher : IEnvelopeDispatcher
    {
        private IRegistration _registration;


        public Envelope Envelope
        {
            get;
            protected set;
        }


        public RabbitEnvelopeDispatcher(IRegistration registration, Envelope envelope)
        {
            _registration = registration;
            this.Envelope = envelope;
        }


        public object Dispatch()
        {
            return this.Dispatch(this.Envelope);
        }

        public object Dispatch(Envelope env)
        {
            // we accept an envelope instead of dispatching the envelope in our
            // state because whoever is consuming us may have mutated it
            DeliveryOutcomes result = DeliveryOutcomes.Null;

            try
            {
                // this may be null, or it may be any other kind of object
                object maybeNull = _registration.Handle(env);

                // by convention, if handlers return nothing, assume acknowledgement
                // if the object is not null and a DeliveryOutcome, cast it
                // else, assume acknowledgement
                if (null == maybeNull) { result = DeliveryOutcomes.Acknowledge; }
                else if (maybeNull is DeliveryOutcomes) { result = (DeliveryOutcomes)maybeNull; }
                else { result = DeliveryOutcomes.Acknowledge; }
            }
            catch (Exception ex)
            {
                _registration.HandleFailed(env, ex);
                result = DeliveryOutcomes.Exception;
            }

            return result;
        }

        public object Fail(Exception ex)
        {
            object result = null;

            try { result = _registration.HandleFailed(this.Envelope, ex); }
            catch { }

            return result;
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using RabbitMQ.Client;

using cmf.bus;
using cmf.bus.berico;

namespace cmf.rabbit
{
    public class RabbitEnvelopeDispatcher : IEnvelopeDispatcher
    {
        private IRegistration _registration;
        private IModel _channel;
        private ulong _deliveryTag;

        public Envelope Envelope
        {
            get;
            protected set;
        }


        public RabbitEnvelopeDispatcher(IRegistration registration, Envelope envelope, IModel channel, ulong deliveryTag)
        {
            _registration = registration;
            _channel = channel;
            _deliveryTag = deliveryTag;

            this.Envelope = envelope;
        }


        public void Dispatch()
        {
            this.Dispatch(this.Envelope);
        }

        public void Dispatch(Envelope env)
        {
            object maybeNull = null;

            try
            {
                // this may be null, or it may be any other kind of object
                maybeNull = _registration.Handle(env);
            }
            catch (Exception ex)
            {
                try { _registration.HandleFailed(env, ex); }
                catch { }
            }

            this.RespondToMessage(maybeNull);
        }

        public void Fail(Exception ex)
        {
            object maybeNull = null;

            try { maybeNull = _registration.HandleFailed(this.Envelope, ex); }
            catch { }

            this.RespondToMessage(maybeNull);
        }


        protected virtual void RespondToMessage(object maybeNull)
        {
            // we accept an envelope instead of dispatching the envelope in our
            // state because whoever is consuming us may have mutated it
            DeliveryOutcomes result = DeliveryOutcomes.Null;

            // by convention, if handlers return nothing, assume acknowledgement
            // if the object is not null and a DeliveryOutcome, cast it
            // else, assume acknowledgement
            if (null == maybeNull) { result = DeliveryOutcomes.Acknowledge; }
            else if (maybeNull is DeliveryOutcomes) { result = (DeliveryOutcomes)maybeNull; }
            else { result = DeliveryOutcomes.Acknowledge; }

            switch (result)
            {
                case DeliveryOutcomes.Acknowledge:
                    _channel.BasicAck(_deliveryTag, false);
                    break;
                case DeliveryOutcomes.Null:
                    _channel.BasicAck(_deliveryTag, false);
                    break;
                case DeliveryOutcomes.Reject:
                    _channel.BasicReject(_deliveryTag, false);
                    break;
                case DeliveryOutcomes.Exception:
                    _channel.BasicNack(_deliveryTag, false, false);
                    break;
            }
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;


namespace cmf.bus.berico
{
    public class DefaultEnvelopeBus : IEnvelopeBus
    {
        protected ITransportProvider _txProvider;

        public IDictionary<int, IInboundEnvelopeProcessor> InboundChain { get; set; }
        public IDictionary<int, IOutboundEnvelopeProcessor> OutboundChain { get; set; }


        public DefaultEnvelopeBus(ITransportProvider transportProvider)
        {
            _txProvider = transportProvider;
            _txProvider.OnEnvelopeReceived += this.Handle_Dispatcher;
        }


        public virtual void Send(Envelope env)
        {
            // guard clause
            if (null == env) { throw new ArgumentNullException("Cannot send a null envelope"); }
            
            // send the envelope through the outbound chain
            this.ProcessOutbound(ref env, this.OutboundChain.Sort());

            // send the envelope to the transport provider
            _txProvider.Send(env);
        }

        public virtual void Register(IRegistration registration)
        {
            // guard clause
            if (null == registration) { throw new ArgumentNullException("Cannot register with a null registration"); }
            
            _txProvider.Register(registration);
        }

        public virtual void Handle_Dispatcher(IEnvelopeDispatcher dispatcher)
        {
            try
            {
                Envelope env = dispatcher.Envelope;

                // send the envelope through the inbound processing chain
                this.ProcessInbound(ref env, this.InboundChain.Sort());

                // the dispatcher encapsulates the logic of giving the envelope to handlers
                dispatcher.Dispatch(env);
            }
            catch (Exception ex)
            {
                dispatcher.Fail(ex);
            }
        }


        protected virtual void ProcessOutbound(ref Envelope env, IEnumerable<IOutboundEnvelopeProcessor> processorChain)
        {
            if ((null == processorChain) || (0 == processorChain.Count())) { return; }

            IDictionary<string, object> processorContext = new Dictionary<string, object>();

            foreach (IOutboundEnvelopeProcessor processor in processorChain)
            {
                processor.ProcessOutbound(ref env, ref processorContext);
            }
        }

        protected virtual void ProcessInbound(ref Envelope env, IEnumerable<IInboundEnvelopeProcessor> processorChain)
        {
            if ((null == processorChain) || (0 == processorChain.Count())) { return; }

            IDictionary<string, object> processorContext = new Dictionary<string, object>();

            foreach (IInboundEnvelopeProcessor processor in processorChain)
            {
                processor.ProcessInbound(ref env, ref processorContext);
            }
        }
    }




    public static class ChainExtensions
    {
        public static IEnumerable<IOutboundEnvelopeProcessor> Sort(this IDictionary<int, IOutboundEnvelopeProcessor> chain)
        {
            return chain
                .OrderBy(kvp => kvp.Key)
                .Select<KeyValuePair<int, IOutboundEnvelopeProcessor>, IOutboundEnvelopeProcessor>(kvp => kvp.Value);
        }

        public static IEnumerable<IInboundEnvelopeProcessor> Sort(this IDictionary<int, IInboundEnvelopeProcessor> chain)
        {
            return chain
                .OrderBy(kvp => kvp.Key)
                .Select<KeyValuePair<int, IInboundEnvelopeProcessor>, IInboundEnvelopeProcessor>(kvp => kvp.Value);
        }
    }
}

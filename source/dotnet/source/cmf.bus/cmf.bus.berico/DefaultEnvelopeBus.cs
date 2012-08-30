using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;


namespace cmf.bus.berico
{
    public class DefaultEnvelopeBus : IEnvelopeBus
    {
        protected SortedDictionary<int, IInboundEnvelopeProcessor> _inboundChain;
        protected SortedDictionary<int, IOutboundEnvelopeProcessor> _outboundChain;
        protected ITransportProvider _txProvider;


        public DefaultEnvelopeBus(
            IDictionary<int, IInboundEnvelopeProcessor> inboundProcessorChain,
            IDictionary<int, IOutboundEnvelopeProcessor> outboundProcessorChain,
            ITransportProvider transportProvider)
        {
            _inboundChain = new SortedDictionary<int, IInboundEnvelopeProcessor>(inboundProcessorChain);
            _outboundChain = new SortedDictionary<int, IOutboundEnvelopeProcessor>(outboundProcessorChain);

            _txProvider = transportProvider;
            _txProvider.OnEnvelopeReceived += new Action<IEnvelopeDispatcher>(_txProvider_OnEnvelopeReceived);
        }


        public virtual void Send(Envelope env)
        {
            // guard clause
            if (null == env) { throw new ArgumentNullException("Cannot send a null envelope"); }
            
            // send the envelope through the outbound chain
            this.ProcessOutbound(ref env, _outboundChain);

            // send the envelope to the transport provider
            _txProvider.Send(env);
        }

        public virtual void Register(IRegistration registration)
        {
            // guard clause
            if (null == registration) { throw new ArgumentNullException("Cannot register with a null registration"); }
            
            _txProvider.Register(registration);
        }


        protected virtual void ProcessOutbound(ref Envelope env, SortedDictionary<int, IOutboundEnvelopeProcessor> processorChain)
        {
            IDictionary<string, object> processorContext = new Dictionary<string, object>();

            foreach (IOutboundEnvelopeProcessor processor in processorChain.Values)
            {
                processor.ProcessOutbound(ref env, ref processorContext);
            }
        }

        protected virtual void ProcessInbound(ref Envelope env, SortedDictionary<int, IInboundEnvelopeProcessor> processorChain)
        {
            IDictionary<string, object> processorContext = new Dictionary<string, object>();

            foreach (IInboundEnvelopeProcessor processor in processorChain.Values)
            {
                processor.ProcessInbound(ref env, ref processorContext);
            }
        }

        protected virtual void _txProvider_OnEnvelopeReceived(IEnvelopeDispatcher dispatcher)
        {
            try
            {
                Envelope env = dispatcher.Envelope;

                // send the envelope through the inbound processing chain
                this.ProcessInbound(ref env, _inboundChain);

                // the dispatcher encapsulates the logic of giving the envelope to handlers
                dispatcher.Dispatch(env);
            }
            catch (Exception ex)
            {
                dispatcher.Fail(ex);
            }
        }
    }
}

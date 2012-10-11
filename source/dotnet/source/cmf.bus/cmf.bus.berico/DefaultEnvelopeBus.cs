using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Common.Logging;

using cmf.bus;


namespace cmf.bus.berico
{
    public class DefaultEnvelopeBus : IEnvelopeBus
    {
        protected ITransportProvider _txProvider;
        protected ILog _log;


        public IDictionary<int, IInboundEnvelopeProcessor> InboundChain { get; set; }
        public IDictionary<int, IOutboundEnvelopeProcessor> OutboundChain { get; set; }


        public DefaultEnvelopeBus(ITransportProvider transportProvider)
        {
            _txProvider = transportProvider;
            _txProvider.OnEnvelopeReceived += this.Handle_Dispatcher;

            _log = LogManager.GetLogger(this.GetType());
        }


        public virtual void Send(Envelope env)
        {
            _log.Debug("Enter Send");
            // guard clause
            if (null == env) { throw new ArgumentNullException("Cannot send a null envelope"); }
            
            // send the envelope through the outbound chain
            this.ProcessOutbound(ref env, this.OutboundChain.Sort());

            // log the headers of the outgoing envelope
            _log.Debug(string.Format("Outgoing headers: {0}", env.Headers.Flatten()));

            // send the envelope to the transport provider
            _txProvider.Send(env);

            _log.Debug("Leave Send");
        }

        public virtual void Register(IRegistration registration)
        {
            _log.Debug("Enter Register");

            // guard clause
            if (null == registration) { throw new ArgumentNullException("Cannot register a null registration"); }
            _txProvider.Register(registration);

            _log.Debug("Leave Register");
        }

        public virtual void Unregister(IRegistration registration)
        {
            _log.Debug("Enter Unregister");

            if (null == registration) { throw new ArgumentNullException("Cannot unregister a null registration"); }
            _txProvider.Unregister(registration);

            _log.Debug("Leave Unregister");
        }

        public virtual void Handle_Dispatcher(IEnvelopeDispatcher dispatcher)
        {
            _log.Debug("Enter Handle_Dispatcher");

            try
            {
                Envelope env = dispatcher.Envelope;

                // send the envelope through the inbound processing chain
                if (this.ProcessInbound(ref env, this.InboundChain.Sort()))
                {
                    // the dispatcher encapsulates the logic of giving the envelope to handlers
                    dispatcher.Dispatch(env);

                    _log.Debug("Dispatched envelope");
                }
            }
            catch (Exception ex)
            {
                _log.Warn("Failed to dispatch envelope; raising EnvelopeFailed event");
                dispatcher.Fail(ex);
            }

            _log.Debug("Leave Handle_Dispatcher");
        }

        public void Dispose()
        {
            _log.Debug("Enter Dispose");
            _txProvider.Dispose();
            _log.Debug("Leave Dispose");
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

        protected virtual bool ProcessInbound(ref Envelope env, IEnumerable<IInboundEnvelopeProcessor> processorChain)
        {
            if ((null == processorChain) || (0 == processorChain.Count())) { return true; }

            bool processed = true;
            IDictionary<string, object> processorContext = new Dictionary<string, object>();

            try
            {
                foreach (IInboundEnvelopeProcessor processor in processorChain)
                {
                    if (!processor.ProcessInbound(ref env, ref processorContext))
                    {
                        processed = false;
                        _log.Warn(string.Format("A processor of type {0} halted the inbound processing chain", processor.GetType().FullName));
                        break;
                    }
                }
            }
            catch (Exception ex)
            {
                _log.Error("Caught an exception while sending an inbound envelope through the processing chain", ex);
                processed = false;
            }

            return processed;
        }
    }




    public static class ChainExtensions
    {
        public static IEnumerable<IOutboundEnvelopeProcessor> Sort(this IDictionary<int, IOutboundEnvelopeProcessor> chain)
        {
            IEnumerable<IOutboundEnvelopeProcessor> sortedChain = new List<IOutboundEnvelopeProcessor>();

            if (null != chain)
            {
                sortedChain = chain
                    .OrderBy(kvp => kvp.Key)
                    .Select<KeyValuePair<int, IOutboundEnvelopeProcessor>, IOutboundEnvelopeProcessor>(kvp => kvp.Value);
            }

            return sortedChain;
        }

        public static IEnumerable<IInboundEnvelopeProcessor> Sort(this IDictionary<int, IInboundEnvelopeProcessor> chain)
        {
            IEnumerable<IInboundEnvelopeProcessor> sortedChain = new List<IInboundEnvelopeProcessor>();

            if (null != chain)
            {
                sortedChain = chain
                    .OrderBy(kvp => kvp.Key)
                    .Select<KeyValuePair<int, IInboundEnvelopeProcessor>, IInboundEnvelopeProcessor>(kvp => kvp.Value);
            }

            return sortedChain;
        }
    }
}

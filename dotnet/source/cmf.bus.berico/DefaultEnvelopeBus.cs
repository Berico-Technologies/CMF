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


        public IDictionary<int, IEnvelopeProcessor> InboundChain { get; set; }
        public IDictionary<int, IEnvelopeProcessor> OutboundChain { get; set; }


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
            
            // create a context
            EnvelopeContext context = new EnvelopeContext(env);

            // send the envelope through the outbound chain
            this.ProcessEnvelope(context, this.OutboundChain.Sort(), () =>
            {
                // send the envelope to the transport provider
                _txProvider.Send(context.Envelope);

                // log the headers of the outgoing envelope
                _log.Debug(string.Format("Outgoing headers: {0}", context.Envelope.Headers.Flatten()));

                _log.Debug("Leave Send");
            });
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
                EnvelopeContext context = new EnvelopeContext(dispatcher.Envelope);

                // send the envelope through the inbound processing chain
                this.ProcessEnvelope(context, this.InboundChain.Sort(), () =>
                {
                    dispatcher.Dispatch(context.Envelope);
                    _log.Debug("Dispatched envelope");
                });
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


        // recursive function that processes envelopes
        protected virtual void ProcessEnvelope(
            EnvelopeContext context, 
            IEnumerable<IEnvelopeProcessor> processorChain, 
            Action processingComplete)
        {
            // if the chain is null or empty, complete processing
            if ((null == processorChain) || (0 == processorChain.Count()))
            {
                processingComplete();
                return;
            }

            // grab the next procesor
            IEnvelopeProcessor nextProcessor = processorChain.First();

            // let it process the envelope and pass its "next" processor: a
            // method that recursively calls this function with the current
            // processor removed
            nextProcessor.ProcessEnvelope(context, () =>
            {
                this.ProcessEnvelope(context, processorChain.Skip(1), processingComplete);
            });
        }
    }




    public static class ChainExtensions
    {
        public static IEnumerable<IEnvelopeProcessor> Sort(this IDictionary<int, IEnvelopeProcessor> chain)
        {
            IEnumerable<IEnvelopeProcessor> sortedChain = new List<IEnvelopeProcessor>();

            if (null != chain)
            {
                sortedChain = chain
                    .OrderBy(kvp => kvp.Key)
                    .Select<KeyValuePair<int, IEnvelopeProcessor>, IEnvelopeProcessor>(kvp => kvp.Value);
            }

            return sortedChain;
        }
    }
}

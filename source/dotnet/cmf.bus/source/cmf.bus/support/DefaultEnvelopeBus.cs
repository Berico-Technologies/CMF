using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Common.Logging;

using cmf.bus.core;


namespace cmf.bus.support
{
    public class DefaultEnvelopeBus : IEnvelopeBus
    {
        public event Action<Envelope, Exception> OnFailedEnvelope;


        protected SortedDictionary<int, IEnvelopeProcessor> _inboundChain;
        protected SortedDictionary<int, IEnvelopeProcessor> _outboundChain;
        protected ITransportProvider _txProvider;
        protected IEnvelopeDispatcher _envDispatcher;
        protected ILog _log;

        private IDictionary<string, IList<IRegistration>> _registrar;
        private object _registrarLock = new object();


        public DefaultEnvelopeBus(
            IDictionary<int, IEnvelopeProcessor> inboundProcessorChain,
            IDictionary<int, IEnvelopeProcessor> outboundProcessorChain,
            ITransportProvider transportProvider,
            IEnvelopeDispatcher envelopeDispatcher)
        {
            _inboundChain = new SortedDictionary<int, IEnvelopeProcessor>(inboundProcessorChain);
            _outboundChain = new SortedDictionary<int, IEnvelopeProcessor>(outboundProcessorChain);

            _envDispatcher = envelopeDispatcher;
            _txProvider = transportProvider;
            _txProvider.OnEnvelopeReceived += new Action<Envelope>(_txProvider_OnEnvelopeReceived);

            _registrar = new Dictionary<string, IList<IRegistration>>();

            _log = LogManager.GetLogger(this.GetType());
        }


        public virtual void Send(Envelope env)
        {
            // guard clause
            if (null == env) { throw new ArgumentNullException("Cannot send a null envelope"); }
            
            // send the envelope through the outbound chain
            this.ProcessEnvelope(env, _outboundChain);

            // send the envelope to the transport provider
            _txProvider.Send(env);
            
            
            _log.Info("Sent envelope: " + env.GetMessageId().ToString());
        }

        public virtual void Register(IRegistration registration)
        {
            // guard clause
            if (null == registration) { throw new ArgumentNullException("Cannot register with a null registration"); }
            
            _log.Debug("Registration received for: " + registration.Topic);
            _txProvider.Register(registration.Topic);
            _log.Info("Registered for: " + registration.Topic);
        }
        
        public virtual void Register(string topic, Func<Envelope, DeliveryOutcome> handler)
        {
            this.Register(new FunctionalRegistration(topic, handler));
        }


        protected virtual void ProcessEnvelope(Envelope env, SortedDictionary<int, IEnvelopeProcessor> processorChain)
        {
            _log.Debug("Envelope before processor chain: " + env.ToString());

            IDictionary<string, object> processorContext = new Dictionary<string, object>();

            foreach (IEnvelopeProcessor processor in processorChain.Values)
            {
                processor.Process(env, processorContext);
            }

            _log.Debug("Envelope after processor chain: " + env.ToString());
        }

        protected virtual void _txProvider_OnEnvelopeReceived(Envelope env)
        {
            try
            {
                // get the registered handlers for topic, or null if there are none
                IEnumerable<IRegistration> registeredHandlers = this.GetRegisteredHandlers(env.GetMessageTopic());

                if (null == registeredHandlers) // there are no handlers?
                {
                    _log.Warn("An envelope arrived for which there were no registered handlers");
                }
                else // we do have handlers
                {
                    // send the envelope through the inbound processing chain
                    this.ProcessEnvelope(env, _inboundChain);

                    // the dispatcher encapsulates the logic of giving the envelope to handlers
                    _envDispatcher.Dispatch(env, registeredHandlers);
                }
            }
            catch (Exception ex)
            {
                _log.Error("Exception while processing an inbound message; will raise the OnFailedEnvelope event", ex);
                this.Raise_OnFailedEnvelope(env, ex);
            }
        }

        protected virtual IEnumerable<IRegistration> GetRegisteredHandlers(string topic)
        {
            IEnumerable<IRegistration> registeredHandlers = null;

            lock (_registrarLock)
            {
                if (_registrar.ContainsKey(topic))
                {
                    registeredHandlers = _registrar[topic];
                }
            }

            return registeredHandlers;
        }


        private void Raise_OnFailedEnvelope(Envelope env, Exception ex)
        {
            if (null != this.OnFailedEnvelope)
            {
                foreach (Delegate d in this.OnFailedEnvelope.GetInvocationList())
                {
                    try { d.DynamicInvoke(env, ex); }
                    catch { }
                }
            }
        }
    }
}

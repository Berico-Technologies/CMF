using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

using cmf.bus.core;

namespace cmf.bus.support
{
    /// <summary>
    /// An in-memory implementation of the envelope bus interface.
    /// <remarks>
    /// <para>
    /// All registrations are held in-memory and all sent envelopes are
    /// delivered only to those registrations. There is no communication 
    /// external to the process using this component, and no processing chain.
    /// </para>
    /// <para>
    /// When an envelope is 'sent', a background thread is created to dispatch
    /// the envelope to any registered consumers. That means that consumers 
    /// handle <i>an envelope</i> sequentially. It also means that consumers 
    /// could be handling multiple envelopes on multiple threads at once.
    /// </para>
    /// <para>
    /// If there are multiple registrations for an envelope and one handler 
    /// throws an exception, the exception is swallowed to ensure all handlers
    /// have an opportunity to handle the envelope.
    /// </para>
    /// <para>
    /// If you wish to change the threading or exception handling behavior, 
    /// override the Dispatch method to do whatever you want.
    /// </para>
    /// </remarks>
    /// </summary>
    public class InMemoryEnvelopeBus : IEnvelopeBus
    {
        public event Action<Envelope, Exception> OnFailedEnvelope;


        protected Dictionary<string, IList<IRegistration>> _registrar;
        
        private object _registrarLock = new object();


        public InMemoryEnvelopeBus()
        {
            _registrar = new Dictionary<string, IList<IRegistration>>();
        }


        public virtual void Send(Envelope env)
        {
            IList<IRegistration> handlerList = null;

            lock (_registrarLock)
            {
                if (_registrar.ContainsKey(env.GetMessageTopic()))
                {
                    handlerList = _registrar[env.GetMessageTopic()];
                }
            }

            this.Dispatch(env, handlerList);
        }

        public virtual void Register(IRegistration registration)
        {
            lock (_registrarLock)
            {
                if (false == _registrar.ContainsKey(registration.Topic))
                {
                    _registrar.Add(registration.Topic, new List<IRegistration>());
                }

                _registrar[registration.Topic].Add(registration);
            }
        }

        public virtual void Register(string topic, Func<Envelope, DeliveryOutcome> handler)
        {
            this.Register(new FunctionalRegistration(topic, handler));
        }


        protected virtual void Dispatch(Envelope env, IList<IRegistration> handlerList)
        {
            Thread dispatchThread = new Thread(new ThreadStart(delegate () {
                handlerList.ToList().ForEach(handler =>
                {
                    try
                    {
                        handler.Handle(env);
                    }
                    catch (Exception ex)
                    {
                        this.Raise_OnFailedEnvelope(env, ex);
                    }
                });
            }));

            dispatchThread.Start();
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

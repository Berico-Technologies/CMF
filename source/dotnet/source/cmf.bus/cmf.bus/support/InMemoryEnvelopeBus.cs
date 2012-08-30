using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

using cmf.bus;

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
        protected List<IRegistration> _registrationList;

        private object _registrationListLock = new object();


        public InMemoryEnvelopeBus()
        {
            _registrationList = new List<IRegistration>();
        }


        public virtual void Send(Envelope env)
        {
            IEnumerable<IRegistration> handlerList = null;

            lock (_registrationListLock)
            {
                handlerList = from reg in _registrationList
                              where ((null != reg.Filter) && (reg.Filter(env)))
                              select reg;
            }

            this.Dispatch(env, handlerList);
        }

        public virtual void Register(IRegistration registration)
        {
            lock (_registrationListLock)
            {
                _registrationList.Add(registration);
            }
        }


        protected virtual void Dispatch(Envelope env, IEnumerable<IRegistration> handlerList)
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
                        handler.HandleFailed(env, ex);
                    }
                });
            }));

            dispatchThread.Start();
        }
    }
}

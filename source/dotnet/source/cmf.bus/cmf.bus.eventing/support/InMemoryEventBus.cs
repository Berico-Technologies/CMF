using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading;

using cmf.bus.core;
using cmf.bus.support;
using cmf.bus.eventing.core;

namespace cmf.bus.eventing.support
{
    public class InMemoryEventBus : IEventBus
    {
        public event Action<Envelope, Exception> OnFailedEnvelope;


        private IDictionary<string, IList<IEventHandler>> _handlers;
        private object _handlerLock = new object();
        private BinaryFormatter _formatter;

        
        public InMemoryEventBus()
        {
            _handlers = new Dictionary<string, IList<IEventHandler>>();
            _formatter = new BinaryFormatter();
        }


        public virtual void Publish(object ev)
        {
            IList<IEventHandler> handlers = this.GetHandlers(ev.GetType());

            this.Dispatch(ev, handlers);
        }

        public virtual void Subscribe(IEventHandler handler)
        {
            this.AddHandler(handler);
        }

        public virtual void Subscribe<TEvent>(Func<TEvent, DeliveryOutcome> handler) where TEvent : class
        {
            this.Subscribe(new TypedEventHandler<TEvent>(handler));
        }


        protected virtual void AddHandler(IEventHandler handler)
        {
            lock (_handlerLock)
            {
                if (false == _handlers.ContainsKey(handler.Topic))
                {
                    _handlers.Add(handler.Topic, new List<IEventHandler>());
                }

                _handlers[handler.Topic].Add(handler);
            }
        }

        protected virtual IList<IEventHandler> GetHandlers(Type eventType)
        {
            IList<IEventHandler> handlers = null;

            lock (_handlerLock)
            {
                if (_handlers.ContainsKey(eventType.FullName))
                {
                    handlers = _handlers[eventType.FullName];
                }
            }

            return handlers;
        }

        protected virtual Envelope CreateEnvelopeForEvent(Type eventType)
        {
            Envelope env = new Envelope();
            env.SetMessageId(Guid.NewGuid());
            env.SetMessageTopic(eventType.FullName);

            return env;
        }

        protected virtual byte[] SerializeEvent(object ev)
        {
            MemoryStream buffer = new MemoryStream();
            _formatter.Serialize(buffer, ev);

            return buffer.ToArray();
        }

        protected virtual void Dispatch(object ev, IList<IEventHandler> handlers)
        {
            Thread dispatchThread = new Thread(new ThreadStart(delegate()
            {
                handlers.ToList().ForEach(handler =>
                {
                    try
                    {
                        handler.Handle(ev);
                    }
                    catch (Exception ex)
                    {
                        this.Raise_OnFailedEnvelope(ev, ex);
                    }
                });
            }));

            dispatchThread.Start();
        }


        private void Raise_OnFailedEnvelope(object ev, Exception ex)
        {
            if (null != this.OnFailedEnvelope)
            {
                Envelope env = this.CreateEnvelopeForEvent(ev.GetType());
                env.Payload = this.SerializeEvent(ev);

                foreach (Delegate d in this.OnFailedEnvelope.GetInvocationList())
                {
                    try { d.DynamicInvoke(env, ex); }
                    catch { }
                }
            }
        }
    }
}

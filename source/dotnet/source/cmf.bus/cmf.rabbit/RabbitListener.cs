using System;
using System.Collections;
using System.Collections.Generic;
using System.DirectoryServices.AccountManagement;
using System.Linq;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using System.Text;

using Common.Logging;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using RabbitMQ.Client.Exceptions;

using cmf.bus;
using cmf.bus.berico;
using cmf.topology;

namespace cmf.rabbit
{
    public class RabbitListener
    {
        public event Action<IEnvelopeDispatcher> OnEnvelopeReceived;
        public event Func<IRegistration, bool> OnClose;


        protected IRegistration _registration;
        protected bool _shouldContinue;
        protected ILog _log;
        protected Exchange _exchange;
        protected IConnection _connection;


        public RabbitListener(IRegistration registration, Exchange exchange, IConnection connection)
        {
            _registration = registration;
            _exchange = exchange;
            _connection = connection;

            _log = LogManager.GetLogger(this.GetType());
        }


        public void Start()
        {
            _shouldContinue = true;

            using (IModel channel = _connection.CreateModel())
            {
                QueueingBasicConsumer consumer = new QueueingBasicConsumer(channel);
                String consumerTag = channel.BasicConsume(_exchange.QueueName, true, consumer.ConsumerTag, _exchange.Arguments, consumer);

                while (_shouldContinue)
                {
                    try
                    {
                        BasicDeliverEventArgs e = consumer.Queue.Dequeue() as BasicDeliverEventArgs;
                        IBasicProperties props = e.BasicProperties;

                        Envelope env = new Envelope();
                        env.Payload = e.Body;
                        foreach (DictionaryEntry entry in props.Headers)
                        {
                            try { env.Headers.Add((string)entry.Key, (string)entry.Value); }
                            catch { }
                        }

                        this.OnEnvelopeReceived(new RabbitEnvelopeDispatcher(_registration, env, channel, e.DeliveryTag));
                    }
                    catch (OperationInterruptedException)
                    {
                        // The consumer was removed, either through
                        // channel or connection closure, or through the
                        // action of IModel.BasicCancel().
                        _shouldContinue = false;
                    }
                    catch { }
                }
            }
        }

        public void Stop()
        {
            _shouldContinue = false;
        }


        protected virtual void Raise_OnCloseEvent(IRegistration registration)
        {
            if (null != this.OnClose)
            {
                try { this.OnClose(registration); }
                catch { }
            }
        }
    }
}

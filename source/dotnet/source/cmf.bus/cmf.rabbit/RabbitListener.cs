using System;
using System.Collections;
using System.Collections.Generic;
using System.DirectoryServices.AccountManagement;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using System.Text;

using Common.Logging;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using RabbitMQ.Client.Exceptions;
using RabbitMQ.Util;

using cmf.bus;
using cmf.bus.berico;
using cmf.rabbit.topology;

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
            _log.Debug("Enter Start");
            _shouldContinue = true;

            using (IModel channel = _connection.CreateModel())
            {
                // first, declare the exchange and queue
                channel.ExchangeDeclare(_exchange.Name, _exchange.ExchangeType, _exchange.IsDurable, _exchange.IsAutoDelete, _exchange.Arguments);
                channel.QueueDeclare(_exchange.QueueName, _exchange.IsDurable, false, _exchange.IsAutoDelete, _exchange.Arguments);
                channel.QueueBind(_exchange.QueueName, _exchange.Name, _exchange.RoutingKey, _exchange.Arguments);

                // next, create a basic consumer
                QueueingBasicConsumer consumer = new QueueingBasicConsumer(channel);

                // and tell it to start consuming messages, storing the consumer tag
                string consumerTag = channel.BasicConsume(_exchange.QueueName, false, consumer);

                _log.Debug("Will now continuously listen for events");
                while (_shouldContinue)
                {
                    try
                    {
                        BasicDeliverEventArgs e = consumer.Queue.Dequeue() as BasicDeliverEventArgs;
                        if (null == e) { continue; }
                        else { this.LogMessage(e); }

                        IBasicProperties props = e.BasicProperties;

                        Envelope env = new Envelope();
                        env.Payload = e.Body;
                        foreach (DictionaryEntry entry in props.Headers)
                        {
                            try
                            {
                                string key = entry.Key as string;
                                string value = Encoding.UTF8.GetString((byte[])entry.Value);
                                _log.Debug("Adding header to envelope: {" + key + ":" + value + "}");

                                env.Headers.Add(key, value);
                            }
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
                _log.Debug("No longer listening for events");

                try { channel.BasicCancel(consumerTag); }
                catch (OperationInterruptedException) { }
            }

            _log.Debug("Leave Start");
        }

        public void Stop()
        {
            _log.Debug("Enter Stop");
            _shouldContinue = false;
            _log.Debug("Leave Stop");
        }


        protected virtual void Raise_OnCloseEvent(IRegistration registration)
        {
            if (null != this.OnClose)
            {
                try { this.OnClose(registration); }
                catch { }
            }
        }

        protected virtual void LogMessage(BasicDeliverEventArgs eventArgs)
        {
            StringBuilder buffer = new StringBuilder();
            buffer.Append("Got a message from the queue: ");

            using (StringWriter writer = new StringWriter(buffer))
            {
                DebugUtil.DumpProperties(eventArgs, writer, 0);
            }

            _log.Debug(buffer.ToString());
        }
    }
}

using System;
using System.Collections.Generic;
using System.DirectoryServices.AccountManagement;
using System.Linq;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using System.Text;

using Common.Logging;
using RabbitMQ.Client;

using cmf.bus;


namespace cmf.rabbit
{
    public class RabbitListener
    {
        public event Func<IRegistration, bool> OnClose;


        protected IRegistration _registration;
        protected IEnumerable<IConnection> _rabbitConnections;


        public RabbitListener(IRegistration registration, IEnumerable<IConnection> rabbitConnections)
        {
            _registration = registration;
            _rabbitConnections = rabbitConnections;
        }


        public void Start()
        {
            
        }

        public void Stop()
        {
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

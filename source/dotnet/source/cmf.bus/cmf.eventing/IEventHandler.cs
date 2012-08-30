using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;


namespace cmf.eventing
{
    public interface IEventHandler
    {
        string Topic { get; }


        void Handle(object ev, IDictionary<string, string> headers);

        void HandleFailed(Envelope env, Exception ex);
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using cmf.bus;


namespace cmf.eventing
{
    public interface IEventHandler<TEvent>
    {
        string Topic { get; }


        object Handle(TEvent ev, IDictionary<string, string> headers);

        object HandleFailed(Envelope env, Exception ex);
    }
}

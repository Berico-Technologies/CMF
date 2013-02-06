using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing.berico
{
    /// <summary>
    /// Defines the interface of a component that can process events on 
    /// their way to and from the client.
    /// <remarks>
    /// If processing should continue, call the provided continuation method.  
    /// 
    /// If processing should stop, do not call the continuation method.  Note 
    /// that the caller is not notified in this case that processing stopped.
    /// 
    /// If processing should stop and the caller should be notified, throw an
    /// exception and it will bubble up to the caller.
    /// </remarks>
    /// </summary>
    public interface IEventProcessor
    {
        void ProcessEvent(EventContext context, Action continueProcessing);
    }
}

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// Adds behaviors to the <see cref="cmf.eventing.IEventBus"/> enabling it to publish events of messages 
    /// over the network before the entire result has been processed.
    /// This can be particularly useful when you have a large amount of data in a response
    /// and would prefer it to be streamed to the recipient in smaller chunks to lower initial latency in response times.
    /// </summary>
    public interface IStreamingEventBus : IEventBus, IStreamingEventConsumer, IStreamingEventProducer
    {
    }
}

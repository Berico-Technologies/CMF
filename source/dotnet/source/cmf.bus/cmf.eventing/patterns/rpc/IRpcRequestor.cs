using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing.patterns.rpc
{
	public interface IRpcRequestor
	{
        object GetResponseTo(object request, TimeSpan timeout);

        TResponse GetResponseTo<TResponse>(object request, TimeSpan timeout) where TResponse : class;

        IEnumerable GatherResponsesTo(object request, TimeSpan timeout);

        IEnumerable<TResponse> GatherResponsesTo<TResponse>(object request, TimeSpan timeout) where TResponse : class;
	}
}

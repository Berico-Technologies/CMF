using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing.patterns.rpc
{
	public interface IRpcRequestor : IDisposable
	{
        /// <summary>
        /// Gets a response to the given request, waiting for a specified span of time.
        /// </summary>
        /// <param name="request">The request to be published</param>
        /// <param name="timeout">The span of time to wait for a response</param>
        /// <param name="expectedTopics">The expected topic on which the response will arive</param>
        /// <returns>The response object, or null if the specified amount of time elapses without receiving a response</returns>
        object GetResponseTo(object request, TimeSpan timeout, string expectedTopic);

        
        /// <summary>
        /// Gets a response to the given request, waiting for a specified span of time.
        /// </summary>
        /// <typeparam name="TResponse">The Type of response to expect</typeparam>
        /// <param name="request">The request to be published</param>
        /// <param name="timeout">The span of time to wait for a response</param>
        /// <returns>The response object, or null if the specified amount of time elapses without receiving a response</returns>
        TResponse GetResponseTo<TResponse>(object request, TimeSpan timeout) where TResponse : class;


        /// <summary>
        /// Gathers zero or more responses to a request.
        /// </summary>
        /// <param name="request">The request to publish</param>
        /// <param name="timeout">The amount of time to wait for responses</param>
        /// <param name="expectedTopics">The expected topics on which the responses will arive</param>
        /// <returns>IEnumerable of responses received, or null if no responses were received within the specified amount of time</returns>
        IEnumerable GatherResponsesTo(object request, TimeSpan timeout, params string[] expectedTopics);

        /// <summary>
        /// Gathers zero or more responses to a request.
        /// </summary>
        /// <typeparam name="TResponse">The Type of responses to expect</typeparam>
        /// <param name="request">The request to publish</param>
        /// <param name="timeout">The amount of time to wait for responses</param>
        /// <returns>IEnumerable of responses received, or null if no responses were received within the specified amount of time</returns>
        IEnumerable<TResponse> GatherResponsesTo<TResponse>(object request, TimeSpan timeout) where TResponse : class;
	}
}

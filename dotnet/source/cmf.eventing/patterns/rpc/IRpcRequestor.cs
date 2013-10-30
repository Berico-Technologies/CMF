using System;
using System.Collections;
using System.Collections.Generic;

namespace cmf.eventing.patterns.rpc
{
    /// <summary>
    /// An interface to define the methods by which an client may send 
    /// RPC messages and receive responses to the same.
    /// </summary>
	public interface IRpcRequestor : IDisposable
	{
        /// <summary>
        /// Sends an RPC message and returns the first response of any type that is received on the specified topic.
        /// </summary>
        /// <param name="request">The request to be published</param>
        /// <param name="timeout">The span of time to wait for a response</param>
        /// <param name="expectedTopic">The expected topic on which the response will arive</param>
        /// <returns>The response object, or null if the specified amount of time elapses without receiving a response</returns>
        object GetResponseTo(object request, TimeSpan timeout, string expectedTopic);

        
        /// <summary>
        /// Sends an RPC message and returns the first response received of an expected type.
        /// </summary>
        /// <typeparam name="TResponse">The Type of response to expect</typeparam>
        /// <param name="request">The request to be published</param>
        /// <param name="timeout">The span of time to wait for a response</param>
        /// <returns>The response object, or null if the specified amount of time elapses without receiving a response</returns>
        TResponse GetResponseTo<TResponse>(object request, TimeSpan timeout) where TResponse : class;


        /// <summary>
        /// Sends an RPC message and returns all responses of any types that are received on a list of topics within a given time period.
        /// </summary>
        /// <param name="request">The request to publish</param>
        /// <param name="timeout">The amount of time to wait for responses</param>
        /// <param name="expectedTopics">The expected topics on which the responses will arive</param>
        /// <returns>IEnumerable of responses received, or null if no responses were received within the specified amount of time</returns>
        IEnumerable GatherResponsesTo(object request, TimeSpan timeout, params string[] expectedTopics);

        /// <summary>
        /// Sends an RPC message and returns all responses of an expected type that are received within a given time period.
        /// </summary>
        /// <typeparam name="TResponse">The Type of responses to expect</typeparam>
        /// <param name="request">The request to publish</param>
        /// <param name="timeout">The amount of time to wait for responses</param>
        /// <returns>IEnumerable of responses received, or null if no responses were received within the specified amount of time</returns>
        IEnumerable<TResponse> GatherResponsesTo<TResponse>(object request, TimeSpan timeout) where TResponse : class;
	}
}

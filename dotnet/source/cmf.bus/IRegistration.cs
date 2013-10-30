using System;
using System.Collections.Generic;

namespace cmf.bus
{
    /// <summary>
    /// Defines the interface that must be implemented by types that wish to 
    /// <see cref="IEnvelopeReceiver">IEnvelopeReceiver.Register</see>. to receive envelopes.
    /// </summary>
    public interface IRegistration
    {
        /// <summary>
        /// Specifies a filter predicate, if any, that should be used to select which received 
        /// envelopes should be forwarded to the Handle method for processing. 
        /// </summary>
        /// <remarks>
        /// <para>Filtering occurs before the envelope processing chain is invoked. Its judicious use can 
        /// therefore eliminate unnecessary processing of unwanted messages and improve performance.</para>
        /// <para>If the filter is null, all envelopes are forwarded to the Handle method.</para>
        /// </remarks>
        Predicate<Envelope> Filter { get; }

        /// <summary>
        /// A key/value set of properties that describe the envelopes to be received. The meaning of 
        /// these values are dependent upon the <see cref="IEnvelopeReceiver">IEnvelopeReceiver</see> 
        /// implementation.
        /// </summary>
        IDictionary<string, string> Info { get; }


        /// <summary>
        /// This method will be invoked when an envelope meeting the Info and Filter criteria is received.
        /// </summary>
        /// <param name="env">The envelope to be handled.</param>
        /// <returns>An object indicating the outcome of handling the envelope. How the return value is 
        /// interpreted is dependent upon the <see cref="IEnvelopeReceiver">IEnvelopeReceiver</see> 
        /// implementation.</returns>
        /// <exception cref="System.Exception">May throw an exception.</exception>
        object Handle(Envelope env);

        /// <summary>
        /// This method will be invoked when an exception occurs attempting to handle an envelope
        /// that meets the registration and filter criteria.
        /// </summary>
        /// <param name="env">The envelope that failed</param>
        /// <param name="ex">The exception that caused the envelope to fail</param>
        /// <returns>An object indicating the outcome of handling the envelope. How the return value is 
        /// interpreted is dependent upon the <see cref="IEnvelopeReceiver">IEnvelopeReceiver</see> 
        /// implementation.</returns>
        /// <exception cref="System.Exception">May throw an exception.</exception>
        object HandleFailed(Envelope env, Exception ex);
    }
}


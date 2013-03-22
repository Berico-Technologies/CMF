using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus
{
    /// <summary>
    /// Implement this interface in order to receive envelopes of interest to you.
    /// </summary>
    public interface IRegistration
    {
        /// <summary>
        /// Provides a way to filter out certain envelopes.
        /// <remarks>
        /// If your predicate is null or returns true, you will receive the 
        /// envelope via the Handle(Envelope) method.
        /// </remarks>
        /// </summary>
        Predicate<Envelope> Filter { get; }

        /// <summary>
        /// Provides the registration information that determines which 
        /// envelopes this registration is interested in.
        /// </summary>
        IDictionary<string, string> Info { get; }


        /// <summary>
        /// The method that handles the envelope.
        /// </summary>
        /// <param name="env">The envelope you should handle</param>
        /// <returns></returns>
        object Handle(Envelope env);

        /// <summary>
        /// The method that handles failed envelopes.
        /// </summary>
        /// <param name="env">The envelope that failed</param>
        /// <param name="ex">The exception that caused the envelope to fail</param>
        object HandleFailed(Envelope env, Exception ex);
    }
}


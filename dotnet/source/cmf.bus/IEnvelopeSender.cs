using System;

namespace cmf.bus
{
    /// <summary>
    /// An interface defining the methods by which an client may send envelopes.
    /// </summary>
    public interface IEnvelopeSender : IDisposable
    {
        /// <summary>
        /// Sends an envelope.
        /// </summary>
        /// <param name="env">The envelope to be sent</param>
        /// <exception cref="System.Exception">May throw an exception.</exception>
        void Send(Envelope env);
    }
}

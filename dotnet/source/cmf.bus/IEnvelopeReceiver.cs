using System;

namespace cmf.bus
{
    /// <summary>
    /// An interface defining the methods by which a client may register to receive 
    /// envelopes and unregister for the same.
    /// </summary>
    public interface IEnvelopeReceiver : IDisposable
    {
        /// <summary>
        ///  Registers a handler to receive envelopes of a particular kind.
        /// </summary>
        /// <param name="registration">The <see cref="IRegistration" /> object 
        /// that describes the kind of envelopes to be received and provides that handler 
        /// method that will receive them. </param>
        /// <exception cref="System.Exception">May throw an exception.</exception>
        void Register(IRegistration registration);

        /// <summary>
        /// Unregisters a handler previously registered.
        /// </summary>
        /// <param name="registration">The <see cref="IRegistration" />
        /// instance previously used to register handler.</param>
        /// <remarks>Any messages already received will continue to be processed.</remarks>
        /// <exception cref="System.Exception">May throw an exception.</exception>
        void Unregister(IRegistration registration);
    }
}

using System;
using System.Collections.Generic;
using cmf.bus;


namespace cmf.eventing
{
    /// <summary>
    /// Defines an interface to be implemented by types that wish to process received events.
    /// </summary>
    /// <seealso cref="IEventConsumer.Subscribe"/>
    public interface IEventHandler
    {
        /// <summary>
        /// The topic of the event which the handler is intended to receive.
        /// </summary>
        ///TODO: Why do we do topic here and type in Java???
        string Topic { get; }

        /// <summary>
        /// This method is invoked when an event of handled type is received. 
        /// It is the method that should handle the received event.
        /// </summary>
        /// <param name="ev"></param>
        /// <param name="headers"></param>
        /// <returns>An object indicating the outcome of handling the event.</returns>
        /// <remarks>How the return value is interpreted is dependent upon the 
        /// <see cref="IEventConsumer" /> implementation</remarks>
        object Handle(object ev, IDictionary<string, string> headers);

        /// <summary>
        /// This method is invoked when an exception occurs attempting to handle 
        /// an envelope that meets the criteria to be handled by this handler. 
        /// </summary>
        /// <param name="env"></param>
        /// <param name="ex"></param>
        /// <returns>An object indicating the outcome of handling the event.</returns>
        /// <remarks><para>How the return value is interpreted is dependent upon the 
        /// <see cref="IEventConsumer" /> implementation.</para>
        /// <para>The relationship between this method and the 
        /// <see cref="IRegistration.HandleFailed" />
        /// method is implementation dependent.</para></remarks>
        object HandleFailed(Envelope env, Exception ex);
    }
}

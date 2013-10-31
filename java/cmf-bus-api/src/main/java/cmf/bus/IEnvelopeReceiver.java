package cmf.bus;

/**
 * An interface defining the methods by which a client may register to receive
 * envelopes and unregister for the same.
 * @see IEnvelopeBus
 */
public interface IEnvelopeReceiver extends IDisposable {

	/**
	 * Registers a handler to receive envelopes of a particular kind. 
	 * 
	 * @param registration The {@link IRegistration} object that describes the kind of 
	 * envelopes to be received and provides that handler method that will receive them.
	 * 
	 * @throws Exception
	 */
    void register(IRegistration registration) throws Exception;

    /**
     * Unregisters a handler previously registered.  Any messages already received will continue to be processed.
     * 
     * @param registration The {@link IRegistration} instance previously used to register handler.
     * 
     * @throws Exception
     */
    void unregister(IRegistration registration) throws Exception;
}

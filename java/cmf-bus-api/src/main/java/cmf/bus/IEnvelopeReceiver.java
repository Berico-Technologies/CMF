package cmf.bus;

/**
 * An interface to define the methods by which an client may register to recieve
 * envelopes and unregister for the same.
 */
public interface IEnvelopeReceiver extends IDisposable {

	/**
	 * Registers a handler to receive envelopes of a particular kind. 
	 * 
	 * @param registration The {@link IRegistration} object that describes the kind of 
	 * envelopes to be received and provides that handler instance that will receive them.
	 * 
	 * @throws Exception
	 */
    void register(IRegistration registration) throws Exception;

    /**
     * Unregisteres a handler previously registered.
     * 
     * @param registration The {@link IRegistration} instance previously used to register handler.
     * 
     * @throws Exception
     */
    void unregister(IRegistration registration) throws Exception;
}

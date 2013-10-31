package cmf.bus;

/**
 * An interface defining the methods by which an client may send envelopes.
 * @see IEnvelopeBus
 */
public interface IEnvelopeSender {

	/**
	 * Sends an envelope.
	 * 
	 * @param envelope The envelope to be sent.
	 * 
	 * @throws Exception
	 */
    void send(Envelope envelope) throws Exception;
}

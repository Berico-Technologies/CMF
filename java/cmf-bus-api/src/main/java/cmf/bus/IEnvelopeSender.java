package cmf.bus;

/**
 * An interface defining the methods by which an client may send envelopes.
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

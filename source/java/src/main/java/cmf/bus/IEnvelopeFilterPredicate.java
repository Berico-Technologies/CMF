package cmf.bus;

public interface IEnvelopeFilterPredicate {

	/**
	 * Offers an opportunity to filter out the envelope before it gets processed.
	 * @param envelope
	 * @return true if you want to receive the envelope, otherwise false
	 */
    boolean filter(Envelope envelope);
}

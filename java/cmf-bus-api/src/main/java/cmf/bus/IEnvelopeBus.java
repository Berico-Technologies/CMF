package cmf.bus;

/**
 * A convenience interface that combines the {@link IEnvelopeReceiver} and {@link IEnvelopeSender} 
 * interfaces together because they are most typically implemented and referenced together.
 */
public interface IEnvelopeBus extends IEnvelopeReceiver, IEnvelopeSender, IDisposable {

}

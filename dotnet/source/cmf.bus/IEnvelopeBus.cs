namespace cmf.bus
{
    /// <summary>
    /// A convenience interface that combines the <see cref="IEnvelopeReceiver">IEnvelopeReceiver</see> 
    /// and <see cref="IEnvelopeSender">IEnvelopeSender</see> interfaces together because they are most 
    /// typically implemented and referenced together.
    /// </summary>
    public interface IEnvelopeBus : IEnvelopeSender, IEnvelopeReceiver
    {
    }
}

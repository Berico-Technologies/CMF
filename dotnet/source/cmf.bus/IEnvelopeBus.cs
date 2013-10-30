namespace cmf.bus
{
    /// <summary>
    /// A convenience interface that combines the <see cref="IEnvelopeReceiver" /> 
    /// and <see cref="IEnvelopeSender" /> interfaces together because they are most 
    /// typically implemented and referenced together.
    /// </summary>
    public interface IEnvelopeBus : IEnvelopeSender, IEnvelopeReceiver
    {
    }
}

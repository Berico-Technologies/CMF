using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.eventing.patterns.streaming
{
    /// <summary>
    /// Working in conjunction with <see cref="IStreamingCollectionHandler"/>, this notifies
    /// interested parties in how many eents have been received before delivering 
    /// the <see cref="System.Collections.Generic.IEnumerable"/> of type TEVENT.
    /// <para>
    /// This can be useful for delivering updates to the UI even though the 
    /// result set has not been fully received yet.
    /// </para>
    /// </summary>
    public interface IStreamingProgressUpdater
    {
        /// <summary>
        /// Called by the <seealso cref="IStreamingCollectionHandler"/> as each event is received and 
        /// placed into the collection.
        /// </summary>
        /// <param name="sequenceId"></param>
        /// <param name="numEventsProcessed"></param>
        void updateProgress(string sequenceId, int numEventsProcessed);
    }
}

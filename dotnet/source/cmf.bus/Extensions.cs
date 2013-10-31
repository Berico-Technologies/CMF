using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus
{
    /// <summary>
    /// Defines extension methods useful in dealing with certain cmf types.
    /// </summary>
    public static class Extensions
    {
        /// <summary>
        /// Returns the key/value pairs of a string/string dictionary as string the form "[key:value, key:value]".
        /// </summary>
        /// <param name="hash">The dictionary to format.</param>
        /// <param name="separator">The separator to be used between key/value pairs.  The default is a comma.</param>
        /// <returns>A string representation of the dictionary.</returns>
        public static string Flatten(this IDictionary<string, string> hash, string separator = ",")
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("[");
            hash.ToList().ForEach(kvp => sb.AppendFormat("{0}{{{1}:{2}}}", separator, kvp.Key, kvp.Value));

            if (sb.Length > 1)
            {
                sb.Remove(1, separator.Length);
            }

            sb.Append("]");
            return sb.ToString();
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.bus
{
    public static class Extensions
    {
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

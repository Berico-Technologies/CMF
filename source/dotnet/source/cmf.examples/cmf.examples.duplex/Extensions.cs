using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.examples.duplex
{
    public static class Extensions
    {
        public static string Flatten(this IDictionary<string, string> headers)
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("[");

            int i = -1;
            for (i = headers.Count-1; i > 0; i--)
            {
                KeyValuePair<string, string> entry = headers.ElementAt(i);
                sb.AppendFormat("{{{0} : {1}}}, ", entry.Key, entry.Value);
            }

            if (i > -1)
            {
                KeyValuePair<string, string> entry = headers.ElementAt(0);
                sb.AppendFormat("{{{0} : {1}}}", entry.Key, entry.Value);
            }

            sb.Append("]");
            return sb.ToString();
        }
    }
}

using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

using Newtonsoft.Json;
using NUnit.Framework;

using cmf.topology;

namespace cmf.tests
{
    [TestFixture]
    public class JsonRoutingInfo
    {
        [TestCase]
        public void GenerateJsonRoutingInfo()
        {
            RoutingInfo info = new RoutingInfo(new RouteInfo[] {
                new RouteInfo(
                    new Exchange("test-exchange-name", "test-host-1", "/test-vhost-1", 5727, "test-routing-key-1"),
                    new Exchange("test-exchange-name", "test-host-1", "/test-vhost-2", 5727, "test-routing-key-1")
                ),
                new RouteInfo(
                    new Exchange("test-exchange-2", "test-host-2", "/", 5727, "test-routing-key-2"),
                    new Exchange("test-exchange-2", "test-host-2", "/", 5727, "test-routing-key-2")
                )
            });

            using (StreamWriter writer = new StreamWriter(@"D:\projects\cmf-routing-info.txt"))
            {
                writer.WriteLine(JsonConvert.SerializeObject(info, Formatting.Indented));
                writer.Flush();
                writer.Close();
            }
        }
    }
}

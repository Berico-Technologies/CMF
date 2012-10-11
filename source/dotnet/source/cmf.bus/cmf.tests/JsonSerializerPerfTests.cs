using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Web.Security;

using Moq;
using Newtonsoft.Json;
using NUnit.Framework;

using cmf.bus;
using cmf.bus.berico;
using cmf.bus.support;
using cmf.eventing.berico;
using cmf.eventing.berico.serializers;
using cmf.eventing.patterns.rpc;
using cmf.security;

namespace cmf.tests
{
    [TestFixture]
    public class JsonSerializerPerfTests
    {
        MockRepository _mocker;
        Mock<ITransportProvider> _txMock;

        DefaultRpcBus _eventBus;
        DefaultEnvelopeBus _envBus;
        JsonEventSerializer _sx;


        [SetUp]
        public void Setup()
        {
            _mocker = new MockRepository(MockBehavior.Loose);

            _txMock = _mocker.Create<ITransportProvider>();
            _envBus = new DefaultEnvelopeBus(_txMock.Object);
            _eventBus = new DefaultRpcBus(_envBus);

            _sx = new JsonEventSerializer();
            _eventBus.InboundChain = new Dictionary<int, IInboundEventProcessor>();
            _eventBus.InboundChain.Add(0, _sx);

            _eventBus.OutboundChain = new Dictionary<int, IOutboundEventProcessor>();
            _eventBus.OutboundChain.Add(0, _sx);
        }


        [TestCase]
        public void Perf()
        {
            List<double> resultList = new List<double>();

            List<TestEvent> evList = new List<TestEvent>();

            for (int i = 0; i < 1000; i++)
            {
                evList.Add(new TestEvent());
            }

            DateTime start = DateTime.Now;
            evList.ForEach(ev => JsonConvert.SerializeObject(ev, Formatting.Indented));
            DateTime stop = DateTime.Now;

            Assert.Pass(stop.Subtract(start).TotalSeconds / 1000 + " seconds");
        }

        [TestCase]
        public void Perf2()
        {
            List<TestEvent> evList = new List<TestEvent>();

            for (int i = 0; i < 1000; i++)
            {
                evList.Add(new TestEvent());
            }

            Stopwatch timer = new Stopwatch();
            timer.Start();
            evList.ForEach(ev => _eventBus.Publish(ev));
            timer.Stop();

            Assert.Pass("Publish took " + timer.Elapsed.TotalSeconds/1000 + " seconds");
        }
    }


    public class TestEvent
    {
        public string A { get; set; }
        public string B { get; set; }
        public string C { get; set; }
        public string D { get; set; }
        public string E { get; set; }

        public TestEvent()
        {
            this.A = Membership.GeneratePassword(128, 0);
            this.B = Membership.GeneratePassword(128, 0);
            this.C = Membership.GeneratePassword(128, 0);
            this.D = Membership.GeneratePassword(128, 0);
            this.E = Membership.GeneratePassword(128, 0);
        }
    }
}

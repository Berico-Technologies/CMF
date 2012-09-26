using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

using Moq;
using NUnit.Framework;

using cmf.bus;
using cmf.bus.berico;
using cmf.bus.support;

namespace cmf.tests
{
    [TestFixture]
    public class DefaultEnvelopeBusTests
    {
        MockRepository _mocker;


        [SetUp]
        public void Setup()
        {
            _mocker = new MockRepository(MockBehavior.Loose);
        }


        [TestCase]
        public void Should_Give_Registrations_To_Transport_Layer()
        {
            Mock<ITransportProvider> txMock = _mocker.Create<ITransportProvider>();
            Mock<IRegistration> regMock = _mocker.Create<IRegistration>();

            DefaultEnvelopeBus bus = new DefaultEnvelopeBus(txMock.Object);
            bus.Register(regMock.Object);

            txMock.Verify(tx => tx.Register(regMock.Object), Times.Once());
        }

        [TestCase]
        public void Should_Dispatch_Envelopes_Even_When_InboundChain_Is_Null()
        {
            Envelope env = new Envelope() { Payload = Encoding.UTF8.GetBytes("Test") };

            DefaultEnvelopeBus bus = new DefaultEnvelopeBus(_mocker.Create<ITransportProvider>().Object);
            bus.InboundChain = null;
            bus.OutboundChain = null;

            // return the envelope when the getter is called
            Mock<IEnvelopeDispatcher> dispatcherMock = _mocker.Create<IEnvelopeDispatcher>();
            dispatcherMock.Setup(d => d.Envelope).Returns(env);

            bus.Handle_Dispatcher(dispatcherMock.Object);

            dispatcherMock.Verify(d => d.Dispatch(env), Times.Once());
        }

        [TestCase]
        public void Should_Send_Envelopes_Even_When_OutboundChain_Is_Null()
        {
            Envelope env = new Envelope() { Payload = Encoding.UTF8.GetBytes("Test") };

            Mock<ITransportProvider> txMock = _mocker.Create<ITransportProvider>();

            DefaultEnvelopeBus bus = new DefaultEnvelopeBus(txMock.Object);
            bus.InboundChain = null;
            bus.OutboundChain = null;

            bus.Send(env);

            txMock.Verify(tx => tx.Send(env), Times.Once());
        }

        public void Should_Send_Outgoing_Envelopes_Through_the_Chain()
        {
        }

        public void Should_Send_Incoming_Envelopes_Through_the_Chain()
        {
        }
    }
}

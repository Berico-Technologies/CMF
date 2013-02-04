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


        [Test]
        public void Should_Give_Registrations_To_Transport_Layer()
        {
            Mock<ITransportProvider> txMock = _mocker.Create<ITransportProvider>();
            Mock<IRegistration> regMock = _mocker.Create<IRegistration>();

            DefaultEnvelopeBus bus = new DefaultEnvelopeBus(txMock.Object);
            bus.Register(regMock.Object);

            txMock.Verify(tx => tx.Register(regMock.Object), Times.Once());
        }

        [Test]
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

        [Test]
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

        [Test]
        public void Should_Send_Outgoing_Envelopes_Through_the_Chain()
        {
            Envelope env = new Envelope() { Payload = Encoding.UTF8.GetBytes("Test") };

            Mock<ITransportProvider> txMock = _mocker.Create<ITransportProvider>();
            Mock<IEnvelopeProcessor> procMock = _mocker.Create<IEnvelopeProcessor>();

            DefaultEnvelopeBus bus = new DefaultEnvelopeBus(txMock.Object);
            bus.InboundChain = null;
            bus.OutboundChain = new Dictionary<int, IEnvelopeProcessor>();
            bus.OutboundChain.Add(0, procMock.Object);

            bus.Send(env);

            procMock.Verify(proc => proc.ProcessEnvelope(It.IsAny<EnvelopeContext>(), It.IsAny<Action<EnvelopeContext>>()));
        }

        [Test]
        public void Should_Send_Incoming_Envelopes_Through_the_Chain()
        {
            Envelope env = new Envelope() { Payload = Encoding.UTF8.GetBytes("Test") };
            EnvelopeContext ctx = new EnvelopeContext(env);

            Mock<ITransportProvider> txMock = _mocker.Create<ITransportProvider>();
            Mock<IEnvelopeProcessor> procMock = _mocker.Create<IEnvelopeProcessor>();
            Mock<IEnvelopeDispatcher> dispatcherMock = _mocker.Create<IEnvelopeDispatcher>();

            dispatcherMock.SetupGet<Envelope>(disp => disp.Envelope).Returns(env);

            DefaultEnvelopeBus bus = new DefaultEnvelopeBus(txMock.Object);
            bus.OutboundChain = null;
            bus.InboundChain = new Dictionary<int, IEnvelopeProcessor>();
            bus.InboundChain.Add(0, procMock.Object);

            txMock.Raise(tx => tx.OnEnvelopeReceived += null, dispatcherMock.Object);

            procMock.Verify(proc => proc.ProcessEnvelope(It.IsAny<EnvelopeContext>(), It.IsAny<Action<EnvelopeContext>>()));
            _mocker.VerifyAll();
        }

        [Test]
        public void Should_Not_Continue_Processing_If_Processor_Does_Not_Call_Continuation()
        {
            Envelope env = new Envelope() { Payload = Encoding.UTF8.GetBytes("Test") };
            EnvelopeContext ctx = new EnvelopeContext(env);

            Mock<ITransportProvider> txMock = _mocker.Create<ITransportProvider>();
            Mock<IEnvelopeProcessor> procMock = _mocker.Create<IEnvelopeProcessor>();
            Mock<IEnvelopeDispatcher> dispatcherMock = _mocker.Create<IEnvelopeDispatcher>();

            dispatcherMock.SetupGet<Envelope>(disp => disp.Envelope).Returns(env);
            
            DefaultEnvelopeBus bus = new DefaultEnvelopeBus(txMock.Object);
            bus.OutboundChain = null;
            bus.InboundChain = new Dictionary<int, IEnvelopeProcessor>();
            bus.InboundChain.Add(0, procMock.Object);

            txMock.Raise(tx => tx.OnEnvelopeReceived += null, dispatcherMock.Object);

            procMock.Verify(proc => proc.ProcessEnvelope(It.IsAny<EnvelopeContext>(), It.IsAny<Action<EnvelopeContext>>()));
            _mocker.VerifyAll();
        }
    }





    public class ExtendedDefaultEnvelopeBus : DefaultEnvelopeBus
    {
        public void PublicProcessEnvelope(EnvelopeContext context, IEnumerable<IEnvelopeProcessor> processorChain, Action<EnvelopeContext> processingComplete)
        {
            this.ProcessEnvelope(context, processorChain, processingComplete);
        }

        protected override void ProcessEnvelope(EnvelopeContext context, IEnumerable<IEnvelopeProcessor> processorChain, Action<EnvelopeContext> processingComplete)
        {
            base.ProcessEnvelope(context, processorChain, processingComplete);
        }
    }
}

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

using Common.Logging;

using cmf.eventing;
using cmf.eventing.patterns.rpc;
using cmf.examples.messages;

namespace cmf.examples.duplex
{
    public partial class DuplexEventForm : Form
    {
        protected ILog _log;


        public IRpcEventBus EventBus { protected get; set; }


        public DuplexEventForm()
        {
            InitializeComponent();

            _log = LogManager.GetLogger(this.GetType());
        }


        public void Handle_EventTypeA(EventTypeA message, IDictionary<string, string> headers)
        {
            _log.Debug("Recieved an event of Type A with headers: " + headers.Flatten());
            try
            {
                this.Log(
                    string.Format("Received an event of type A, message: {0}{1}\t{2}",
                        message.Message,
                        Environment.NewLine,
                        headers.Flatten()
                    )
                );
            }
            catch (Exception ex)
            {
                this.Log(ex.ToString());
            }
        }

        public void Handle_EventTypeB(EventTypeB message, IDictionary<string, string> headers)
        {
            _log.Debug("Recieved an event of Type B with headers: " + headers.Flatten());

            try
            {
                this.Log(
                    string.Format("Received an event of type B, message: {0}{1}\t{2}",
                        message.Message,
                        Environment.NewLine,
                        headers.Flatten()
                    )
                );
            }
            catch (Exception ex)
            {
                this.Log(ex.ToString());
            }
        }

        public void Handle_ExampleRequest(ExampleRequest request, IDictionary<string, string> headers)
        {
            ExampleResponse response = new ExampleResponse();
            response.OriginalMessage = request.Message;
            response.ResponseMessage = string.Format("{0}, eh?  Fascinating.", request.Message);

            try
            {
                this.Log(string.Format("Responding to {0} with {1}", response.OriginalMessage, response.ResponseMessage));
                this.EventBus.RespondTo(headers, response);
            }
            catch (Exception ex)
            {
                this.Log(ex.ToString());
            }
        }


        protected void Log(string message)
        {
            StringBuilder sb = new StringBuilder(_output.Text);
            sb.Insert(0, string.Format("{0}{1}", message, Environment.NewLine));

            _output.Text = sb.ToString();
        }

        protected void InformUser(string message)
        {
            MessageBox.Show(message, "Information", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        protected void WarnUser(string message)
        {
            MessageBox.Show(message, "WARNING", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
        

        private void _typeAChk_CheckedChanged(object sender, EventArgs e)
        {
            if (_typeAChk.Checked)
            {
                try
                {
                    this.EventBus.Subscribe<EventTypeA>(this.Handle_EventTypeA);
                    _typeAChk.Enabled = false;
                }
                catch (Exception ex)
                {
                    this.Log(ex.ToString());
                }
            }
        }

        private void _typeBChk_CheckStateChanged(object sender, EventArgs e)
        {
            if (_typeBChk.Checked)
            {
                try
                {
                    this.EventBus.Subscribe<EventTypeB>(this.Handle_EventTypeB);
                    _typeBChk.Enabled = false;
                }
                catch (Exception ex)
                {
                    this.Log(ex.ToString());
                }
            }
        }

        private void _publishBtn_Click(object sender, EventArgs e)
        {
            string eventType = _eventTypeCbo.Text;
            if (string.IsNullOrEmpty(eventType))
            {
                this.InformUser("You must select the type of event to publish before attempting to publish an event");
                return;
            }

            string message = _eventTxt.Text;
            if (string.IsNullOrEmpty(message))
            {
                this.InformUser("You should add a message to the event before publishing it");
                return;
            }

            object eventMsg = null;

            switch (eventType)
            {
                case "cmf.examples.messages.EventTypeA":
                    eventMsg = new EventTypeA() { Message = message };
                    break;
                case "cmf.examples.messages.EventTypeB":
                    eventMsg = new EventTypeB() { Message = message };
                    break;
                default:
                    this.WarnUser("I don't know how to send the type of event you selected.  This should not have happened.");
                    break;
            }

            try
            {
                this.EventBus.Publish(eventMsg);
            }
            catch (Exception ex)
            {
                this.Log(ex.ToString());
            }
        }

        private void _clearLogBtn_Click(object sender, EventArgs e)
        {
            _output.Clear();
        }

        private void DuplexEventForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            this.EventBus.Dispose();
        }

        private void _requestBtn_Click(object sender, EventArgs e)
        {
            string msg = _requestMessage.Text;

            if (string.IsNullOrEmpty(msg))
            {
                this.InformUser("Please add a request message.  Just type in whatever you like.");
                return;
            }

            ExampleRequest request = new ExampleRequest(msg);
            TimeSpan fiveSeconds = new TimeSpan(0, 0, 5);

            try
            {
                ExampleResponse response = this.EventBus.GetResponseTo<ExampleResponse>(request, fiveSeconds);

                if (null == response)
                {
                    this.Log("Did not get a response to the request within 5 seconds.  Ensure there is another .NET Consumer & Producer running");
                }
                else
                {
                    this.Log(string.Format("Got a response to '{0}': '{1}'", response.OriginalMessage, response.ResponseMessage));
                }
            }
            catch (Exception ex)
            {
                this.Log("Could not send the request: " + ex.ToString());
            }
        }

        private void DuplexEventForm_Load(object sender, EventArgs e)
        {
            this.EventBus.Subscribe<ExampleRequest>(this.Handle_ExampleRequest);
        }
    }
}

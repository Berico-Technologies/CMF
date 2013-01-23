using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace cmf.examples.messages
{
    public class ExampleRequest
    {
        public string Message { get; set; }


        public ExampleRequest()
        {
        }

        public ExampleRequest(string message)
        {
            this.Message = message;
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;

using Spring.Context;
using Spring.Context.Support;

namespace cmf.examples.duplex
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            IApplicationContext springContainer = ContextRegistry.GetContext();
            DuplexEventForm form = springContainer.GetObject(typeof(DuplexEventForm).FullName) as DuplexEventForm;
            
            Application.Run(form);
        }
    }
}

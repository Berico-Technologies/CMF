using System;
using System.Collections.Generic;
using System.DirectoryServices.AccountManagement;
using System.Linq;
using System.Text;

using NUnit.Framework;

namespace cmf.tests
{
    [TestFixture]
    public class UserPrincipalTests
    {
        [Test]
        public void Show_Outputs()
        {
            UserPrincipal user = UserPrincipal.Current;
            Console.WriteLine(user.Name);
        }
    }
}

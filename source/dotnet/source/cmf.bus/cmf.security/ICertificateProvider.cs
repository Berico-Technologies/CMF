using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography.X509Certificates;
using System.Text;

namespace cmf.security
{
    public interface ICertificateProvider
    {
        X509Certificate2 GetCertificate();

        X509Certificate2 GetCertificateFor(string distinguishedName);
    }
}
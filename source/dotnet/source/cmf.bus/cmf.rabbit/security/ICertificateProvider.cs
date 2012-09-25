using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography.X509Certificates;
using System.Text;

namespace cmf.rabbit.security
{
    public interface ICertificateProvider
    {
        X509Certificate2 GetCertificate();
    }
}

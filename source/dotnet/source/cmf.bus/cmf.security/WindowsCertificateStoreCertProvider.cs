using System;
using System.Collections.Generic;
using System.DirectoryServices.AccountManagement;
using System.Linq;
using System.Security.Cryptography.X509Certificates;
using System.Text;

namespace cmf.security
{
    public class WindowsCertificateStoreCertProvider : ICertificateProvider
    {
        public X509Certificate2 GetCertificate(string hostname, int port)
        {
            // I've imported my certificate into my certificate store 
            // (the Personal/Certificates folder in the certmgr mmc snap-in)
            // Let's open that store right now.
            X509Store certStore = new X509Store(StoreName.My, StoreLocation.CurrentUser);
            certStore.Open(OpenFlags.ReadOnly);

            // the DN I get is CN=name,CN=Users,DC=example,DC=com
            // but the DN on the cert has spaces after each comma
            string spacedDN = UserPrincipal.Current.DistinguishedName.Replace(",", ", ");

            // get and store the certificate
            return certStore.Certificates
                .Find(
                    X509FindType.FindBySubjectDistinguishedName,
                    spacedDN,
                    true)
                .OfType<X509Certificate2>()
                .FirstOrDefault();
        }
    }
}

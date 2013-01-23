using System;
using System.Collections.Generic;
using System.DirectoryServices.AccountManagement;
using System.Linq;
using System.Security.Cryptography.X509Certificates;
using System.Text;

using Common.Logging;

namespace cmf.security
{
    public class WindowsCertificateStoreCertProvider : ICertificateProvider
    {
        protected ILog _log;

        private IDictionary<string, X509Certificate2> _cache;
        private object _cacheLock = new object();


        public WindowsCertificateStoreCertProvider()
        {
            _cache = new Dictionary<string, X509Certificate2>();

            _log = LogManager.GetLogger(this.GetType());
        }


        public virtual X509Certificate2 GetCertificate()
        {
            _log.Debug("Enter GetCertificate()");

            X509Certificate2 cert = null;

            // the DN I get is CN=name,CN=Users,DC=example,DC=com
            // but the DN on the cert has spaces after each comma
            string spacedDN = UserPrincipal.Current.DistinguishedName.Replace(",", ", ");
            _log.Debug(string.Format("Looking for certificate with subject: {0}", spacedDN));

            cert = this.GetCertificateFromCache(spacedDN);

            if (null == cert)
            {
                _log.Debug("Certificate not cached: opening certificate store to find certificate");

                // I've imported my certificate into my certificate store 
                // (the Personal/Certificates folder in the certmgr mmc snap-in)
                // Let's open that store right now.
                X509Store certStore = new X509Store(StoreName.My, StoreLocation.CurrentUser);
                certStore.Open(OpenFlags.ReadOnly);

                // for debugging purposes, write out all the user's certs
                if (_log.IsDebugEnabled)
                {
                    foreach (X509Certificate2 c in certStore.Certificates)
                    {
                        _log.Debug(string.Format("Certificate store has a certificate with subject: {0}", c.Subject));
                    }
                }

                // get and store the certificate
                cert = certStore.Certificates
                    .Find(
                        X509FindType.FindBySubjectDistinguishedName,
                        spacedDN,
                        true)
                    .OfType<X509Certificate2>()
                    .FirstOrDefault();

                if (null != cert)
                {
                    _log.Debug("Found the desired certificate in the user's store: adding to cache");
                    this.CacheCertificate(spacedDN, cert);
                }
            }

            _log.Debug("Leave GetCertificate()");
            return cert;
        }

        public virtual X509Certificate2 GetCertificateFor(string distinguishedName)
        {
            _log.Debug(string.Format("Enter GetCertificateFor({0})", distinguishedName));

            X509Certificate2 cert = null;

            cert = this.GetCertificateFromCache(distinguishedName);

            if (null == cert)
            {
                // remove any spaces after commas
                string dn = distinguishedName.Replace(", ", ",");

                PrincipalContext ctx = new PrincipalContext(ContextType.Domain);
                UserPrincipal user = UserPrincipal.FindByIdentity(ctx, IdentityType.DistinguishedName, dn);

                if (null != user)
                {
                    cert = user.Certificates
                        .Find(
                            X509FindType.FindBySubjectDistinguishedName,
                            distinguishedName,
                            true
                        )
                        .OfType<X509Certificate2>()
                        .FirstOrDefault();

                    if (null != cert)
                    {
                        this.CacheCertificate(distinguishedName, cert);
                    }
                }
            }

            _log.Debug(string.Format("Leave GetCertificateFor({0})", distinguishedName));
            return cert;
        }

        protected virtual X509Certificate2 GetCertificateFromCache(string distinguishedName)
        {
            X509Certificate2 cert = null;

            lock (_cacheLock)
            {
                if (_cache.ContainsKey(distinguishedName))
                {
                    _log.Debug("Certificate cache contains cert for " + distinguishedName);
                    cert = _cache[distinguishedName];
                }
            }

            return cert;
        }

        protected virtual void CacheCertificate(string distinguishedName, X509Certificate2 cert)
        {
            _log.Debug("Caching cert for " + distinguishedName);
            lock (_cacheLock)
            {
                _cache[distinguishedName] = cert;
            }
        }
    }
}

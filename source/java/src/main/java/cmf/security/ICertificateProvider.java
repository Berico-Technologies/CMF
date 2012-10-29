package cmf.security;

import java.security.cert.X509Certificate;

public interface ICertificateProvider {

    X509Certificate getCertificateFor(String distinguishedName);

    CredentialHolder getCredentials();
}

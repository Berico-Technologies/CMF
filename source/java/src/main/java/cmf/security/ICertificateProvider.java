package cmf.security;

import java.security.cert.X509Certificate;

public interface ICertificateProvider {

	CredentialHolder getCredentials();
	
	X509Certificate getCertificateFor(String distinguishedName);
}

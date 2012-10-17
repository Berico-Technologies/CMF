package cmf.security;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

public interface ICertificateProvider {

	KeyPair getCertificate();
	
	X509Certificate getCertificateFor(String distinguishedName);
}

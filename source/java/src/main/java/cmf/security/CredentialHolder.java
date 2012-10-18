package cmf.security;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * Holds a certificate and its associated private key.
 * @author jruiz
 *
 * @param <X509Certificate>
 * @param <PrivateKey>
 */
public class CredentialHolder {

	protected X509Certificate cert;
	protected PrivateKey privateKey;
	
	
	public X509Certificate getCertificate() { return this.cert; }
	public PrivateKey getPrivateKey() { return this.privateKey; }
	
	
	public CredentialHolder(X509Certificate certificate, PrivateKey privateKey) {
		this.cert = certificate;
		this.privateKey = privateKey;
	}
}

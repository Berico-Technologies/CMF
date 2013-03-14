package cmf.bus.default.security;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * Holds a certificate and its associated private key.
 * 
 * @author jruiz
 * 
 * @param <X509Certificate>
 * @param <PrivateKey>
 */
public class CredentialHolder {

    protected X509Certificate cert;
    protected PrivateKey privateKey;

    public CredentialHolder(X509Certificate certificate, PrivateKey privateKey) {
        cert = certificate;
        this.privateKey = privateKey;
    }

    public X509Certificate getCertificate() {
        return cert;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}

package cmf.bus.default.security;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PKCS12CertificateProvider implements ICertificateProvider {

	protected String pathToP12File;
	protected String password;
	protected Logger log;
	
	
	public PKCS12CertificateProvider(String pathToP12File, String password) {
		this.pathToP12File = pathToP12File;
		this.password = password;
		
		log = LoggerFactory.getLogger(this.getClass());
	}
	
	
	@Override
	public CredentialHolder getCredentials() {
		CredentialHolder credentials = null;
		
		try {
			char[] passphrase = this.password.toCharArray();
			
			KeyStore keyStore = KeyStore.getInstance("pkcs12");
			keyStore.load(new FileInputStream(this.pathToP12File), passphrase);
			
			Enumeration<String> aliasList = keyStore.aliases();
			
			Key key = null;
			X509Certificate cert = null;
			
			while (aliasList.hasMoreElements()) {
				String alias = aliasList.nextElement();
				
				key = keyStore.getKey(alias, passphrase);
				cert = (X509Certificate)keyStore.getCertificate(alias);
				
				credentials = new CredentialHolder(cert, (PrivateKey)key);
			}
		} catch (Exception e) {
			log.error("Exception getting credentials", e);
		}
		
		return credentials;
	}
}

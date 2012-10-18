//package cmf.security;
//
//import java.io.FileInputStream;
//import java.security.Key;
//import java.security.KeyPair;
//import java.security.KeyStore;
//import java.security.PrivateKey;
//import java.security.cert.Certificate;
//import java.security.cert.X509Certificate;
//import java.util.Enumeration;
//
//public class PKCS12CertificateProvider implements ICertificateProvider {
//
//	protected String pathToP12File;
//	protected String password;
//	
//	
//	public PKCS12CertificateProvider(String pathToP12File, String password) {
//		this.pathToP12File = pathToP12File;
//		this.password = password;
//	}
//	
//	
//	@Override
//	public CredentialHolder getCertificate() {
//		KeyPair pair;
//		
//		try {
//			KeyStore keyStore = KeyStore.getInstance("pkcs12");
//			keyStore.load(new FileInputStream(this.pathToP12File), this.password.toCharArray());
//			
//			Enumeration<String> aliasList = keyStore.aliases();
//			
//			Key key = null;
//			Certificate cert = null;
//			
//			while (aliasList.hasMoreElements()) {
//				String alias = aliasList.nextElement();
//				
//				key = keyStore.getKey(alias, password);
//				cert = keyStore.getCertificate(alias);
//				
//				KeyPair keyPair = new KeyPair(cert.getPublicKey(), (PrivateKey)key);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public X509Certificate getCertificateFor(String distinguishedName) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}

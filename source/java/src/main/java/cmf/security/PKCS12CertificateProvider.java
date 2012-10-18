package cmf.security;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.Subject;

import com.sun.security.auth.module.Krb5LoginModule;

public class PKCS12CertificateProvider implements ICertificateProvider {

	protected String pathToP12File;
	protected String password;
	
	
	public PKCS12CertificateProvider(String pathToP12File, String password) {
		this.pathToP12File = pathToP12File;
		this.password = password;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return credentials;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public X509Certificate getCertificateFor(final String distinguishedName) {
		X509Certificate credentials = null;
		
		try {
			Krb5LoginModule loginModule = new Krb5LoginModule();
	
	        Subject subject = new Subject();
			Map state = new HashMap();
			Map<String, Object> options = new HashMap<String, Object>();
	
			options.put("useTicketCache", "true");
			options.put("doNotPrompt", "true");
			options.put("debug", "true");
			options.put("useFirstPass", "false");
			options.put("storePass", "false");
			options.put("clearPass", "true");
			options.put("renewTGT", "true");
			
			loginModule.initialize(subject, null, state, options);
			
			if (loginModule.login()) {
				loginModule.commit();
			}
			
			credentials = Subject.doAs(subject, new PrivilegedAction<X509Certificate>() {

				@Override
				public X509Certificate run() {
					
					X509Certificate cert = null;
					
					try {
						Hashtable<String, Object> props = new Hashtable<String, Object>();
						props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
						props.put(Context.PROVIDER_URL, "ldap://dc01.johnruiz.com/DC=johnruiz,DC=com");
						props.put(Context.SECURITY_AUTHENTICATION, "GSSAPI");
					
					    /* Create initial context */
					    DirContext ctx = new InitialDirContext(props);

					    SearchControls controls = new SearchControls();
					    controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
					    controls.setDerefLinkFlag(true);
					    
					    String dn = distinguishedName.replaceAll(", ", ",");
					    String filter = String.format("(&(objectCategory=person)(objectClass=user)(distinguishedName=%s))", dn);
					    
					    NamingEnumeration<SearchResult> results = ctx.search( 
					    		"", 
					    		filter, 
					    		controls);
					    
					    SearchResult result = results.next();
					    
				    	System.out.println("Name: " + result.getName());
				    	System.out.println("NameInNamespace: " + result.getNameInNamespace());
				    	
				    	Attributes attrs = result.getAttributes();
				    	CertificateFactory factory = CertificateFactory.getInstance("X.509");
				    	Attribute certAttr = attrs.get("userCertificate");
				    	
				    	if (null != certAttr && certAttr.size() > 0) {
				    		ByteArrayInputStream input = new ByteArrayInputStream((byte[])certAttr.get());
				    		cert = (X509Certificate)factory.generateCertificate(input);
				    	
				    		System.out.println("Found certificate issued by: " + cert.getIssuerDN().getName());
				    	}
					    // Close the context when we're done
					    ctx.close();
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}
					
					return cert;
				}
			});
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		return credentials;
	}
}

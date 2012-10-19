package cmf.security;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

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

public class WindowsCertificateProvider implements ICertificateProvider {

	protected String ldapProviderUrl;
	
	
	public WindowsCertificateProvider(String ldapProviderUrl) {
		this.ldapProviderUrl = ldapProviderUrl;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public CredentialHolder getCredentials() {
		
		CredentialHolder credentials = null;
		
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
			
			credentials = Subject.doAs(subject, new PrivilegedAction<CredentialHolder>() {

				@Override
				public CredentialHolder run() {
					
					CredentialHolder credentials = null;
					
					try {
						Hashtable<String, Object> props = new Hashtable<String, Object>();
						props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
						props.put(Context.PROVIDER_URL, ldapProviderUrl);
						props.put(Context.SECURITY_AUTHENTICATION, "GSSAPI");
					
					    /* Create initial context */
					    DirContext ctx = new InitialDirContext(props);

					    SearchControls controls = new SearchControls();
					    controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
					    controls.setDerefLinkFlag(true);
					    
					    String filter = String.format("(&(objectCategory=person)(objectClass=user)(sAMAccountName=%s))", System.getProperty("user.name"));
					    
					    NamingEnumeration<SearchResult> results = ctx.search( 
					    		"", 
					    		filter, 
					    		controls);
					    
					    SearchResult result = results.next();
					    
				    	System.out.println("Name: " + result.getName());
				    	System.out.println("NameInNamespace: " + result.getNameInNamespace());
				    	for (NamingEnumeration<? extends javax.naming.directory.Attribute> e = result.getAttributes().getAll(); e.hasMoreElements();) {
				    		javax.naming.directory.Attribute attr = e.next();
				    		System.out.println("|-- Found attribute named: " + attr.getID());
				    	}
				    	
				    	String distinguishedName = result.getNameInNamespace();
				    	
					    // Close the context when we're done
					    ctx.close();
					    
					    System.out.println("DistinguishedName: " + distinguishedName);
					    
					    try {
							KeyStore keyStore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
						    keyStore.load(null, null);
						
						    String alias = distinguishedName.substring(0, distinguishedName.indexOf(','));
						    alias = alias.replaceFirst("CN=", "");
						    System.out.println("alias: " + alias);
						    
						    PrivateKey key = (PrivateKey) keyStore.getKey(alias, null);
						    Certificate[] chain = keyStore.getCertificateChain(alias);
						    X509Certificate cert = (X509Certificate) chain[0];
						    
						    if (null ==cert) {
						    	System.out.println("No certificates found with alias: " + alias);
						    }
						    else {
						    	System.out.println("Found certificate issued by: " +cert.getIssuerX500Principal().getName());
						    	credentials = new CredentialHolder(cert, key);
						    }
						}
						catch(Exception ex) {
							ex.printStackTrace();
						}
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}
					
					return credentials;
				}
			});
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		return credentials;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
						props.put(Context.PROVIDER_URL, ldapProviderUrl);
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

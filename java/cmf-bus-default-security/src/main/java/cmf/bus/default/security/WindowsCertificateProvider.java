package cmf.bus.default.security;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




import com.sun.security.auth.module.Krb5LoginModule;

public class WindowsCertificateProvider implements ICertificateProvider {

	protected IUserInfoRepository userInfoRepo;
	protected Logger log;
	
	
	public WindowsCertificateProvider(IUserInfoRepository userInfoRepository) {
		this.userInfoRepo = userInfoRepository;
		
		this.log = LoggerFactory.getLogger(this.getClass());
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
				    	String distinguishedName = userInfoRepo.getDistinguishedName(System.getProperty("user.name"));
				    	
					    log.debug("DistinguishedName: {}", distinguishedName);
					    
					    try {
							KeyStore keyStore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
						    keyStore.load(null, null);
						
						    String alias = distinguishedName.substring(0, distinguishedName.indexOf(','));
						    alias = alias.replaceFirst("CN=", "");
						    log.debug("alias: {}", alias);
						    
						    PrivateKey key = (PrivateKey) keyStore.getKey(alias, null);
						    Certificate[] chain = keyStore.getCertificateChain(alias);
						    X509Certificate cert = (X509Certificate) chain[0];
						    
						    if (null ==cert) {
						    	log.debug("No certificates found with alias: {}", alias);
						    }
						    else {
						    	log.debug("Found certificate issued by: {}", cert.getIssuerX500Principal().getName());
						    	credentials = new CredentialHolder(cert, key);
						    }
						}
						catch(Exception ex) {
							log.error("Exception while retrieving credentials from Windows Certificate Store", ex);
						}
					}
					catch(Exception ex) {
						log.error("Exception attempting to get credentials", ex);
					}
					
					return credentials;
				}
			});
		} catch (Exception e) {
			log.error("Exception attempting to get credentials", e);
		}
		
		return credentials;
	}
}

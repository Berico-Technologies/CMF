package cmf.security;

import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * Just a basic Repo for development purposes when there is no ldap or certs
 * @author camechis
 *
 */
public class InMemoryUserInfoRepository implements IUserInfoRepository {

	private Map<String,String> mapping = null;
	
	
	public InMemoryUserInfoRepository( Map<String,String> mapping ) {
		this.mapping = mapping;
	}
	
	
	
	@Override
	public String getDistinguishedName(String accountName) throws Exception {
		String dn = mapping.get(accountName);
		if( dn == null ) {
			return accountName;
		}
		return dn;
	}

	@Override
	public X509Certificate getPublicCertificateFor(String distinguishedName)
			throws Exception {
		return null;
	}

}

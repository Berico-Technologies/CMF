package cmf.security;

import java.security.cert.X509Certificate;

public interface IUserInfoRepository {
	String getDistinguishedName(final String accountName) throws Exception;
	X509Certificate getPublicCertificateFor(final String distinguishedName) throws Exception;
}

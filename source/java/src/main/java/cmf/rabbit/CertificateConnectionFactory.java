package cmf.rabbit;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import cmf.rabbit.topology.Exchange;

import com.rabbitmq.client.Connection;

public class CertificateConnectionFactory implements RabbitConnectionFactory {

	protected String pathToClientCert;
	protected String password;
	
	
	public CertificateConnectionFactory(String pathToClientCertificate ) {
	}
	
	
	@Override
	public Connection connectTo(Exchange exchange) throws Exception {
		
		char[] passphrase = this.password.toCharArray();
		
		KeyStore ks = KeyStore.getInstance("PKCS12");
		FileInputStream fs = new FileInputStream(this.pathToClientCert);
		ks.load(fs, passphrase);
		
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
        
        KeyStore tks = KeyStore.getInstance("JKS");
        tks.load(new FileInputStream("/path/to/trustStore"), passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(tks);

        SSLContext c = SSLContext.getInstance("SSLv3");
        c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5671);
        factory.useSslProtocol(c);

        return factory.newConnection();
	}
}

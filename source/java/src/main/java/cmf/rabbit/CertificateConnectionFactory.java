package cmf.rabbit;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import cmf.rabbit.topology.Exchange;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultSaslConfig;

public class CertificateConnectionFactory implements RabbitConnectionFactory {

	protected String pathToClientCert;
	protected String password;
	protected String pathToRemoteCertStore;
	
	public CertificateConnectionFactory(String pathToClientCertificate, String password, String pathToRemoteCertStore) {
		this.pathToClientCert = pathToClientCertificate;
		this.password = password;
		this.pathToRemoteCertStore = pathToRemoteCertStore;
	}
	
	
	@Override
	public Connection connectTo(Exchange exchange) throws Exception {
		
		char[] keyPassphrase = this.password.toCharArray();
		
        KeyStore clientCertStore = KeyStore.getInstance("PKCS12");
        clientCertStore.load(new FileInputStream(this.pathToClientCert), keyPassphrase);
		
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(clientCertStore, keyPassphrase);
        
        KeyStore remoteCertStore = KeyStore.getInstance("JKS");
        remoteCertStore.load(new FileInputStream(this.pathToRemoteCertStore), null);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(remoteCertStore);

        SSLContext c = SSLContext.getInstance("SSLv3");
        c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(exchange.getHostName());
        factory.setPort(exchange.getPort());
        factory.setSaslConfig(DefaultSaslConfig.EXTERNAL);
        factory.useSslProtocol(c);
        
        return factory.newConnection();
	}
}

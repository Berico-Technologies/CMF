package cmf.bus.default.rabbit;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import cmf.bus.default.rabbit.topology.Exchange;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultSaslConfig;

public class CertificateChannelFactory extends BaseChannelFactory {

	protected String password;
    protected String pathToClientCert;
    protected String pathToRemoteCertStore;

    public CertificateChannelFactory(String pathToClientCertificate, String password, String pathToRemoteCertStore) {
        pathToClientCert = pathToClientCertificate;
        this.password = password;
        this.pathToRemoteCertStore = pathToRemoteCertStore;
    }
	
	@Override
	public Connection getConnection(Exchange exchange) throws Exception {
		
		char[] keyPassphrase = password.toCharArray();

        KeyStore clientCertStore = KeyStore.getInstance("PKCS12");
        clientCertStore.load(new FileInputStream(pathToClientCert), keyPassphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(clientCertStore, keyPassphrase);

        KeyStore remoteCertStore = KeyStore.getInstance("JKS");
        remoteCertStore.load(new FileInputStream(pathToRemoteCertStore), null);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(remoteCertStore);

        SSLContext c = SSLContext.getInstance("SSLv3");
        c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(exchange.getHostName());
        factory.setPort(exchange.getPort());
        factory.setVirtualHost(exchange.getVirtualHost());
        factory.setSaslConfig(DefaultSaslConfig.EXTERNAL);
        factory.useSslProtocol(c);
        //factory.setRequestedHeartbeat(HEARTBEAT_INTERVAL);
        
        return factory.newConnection();
	}

}
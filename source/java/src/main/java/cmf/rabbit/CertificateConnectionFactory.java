package cmf.rabbit;

import java.security.KeyStore;

import cmf.rabbit.topology.Exchange;

import com.rabbitmq.client.Connection;

public class CertificateConnectionFactory implements RabbitConnectionFactory {

	protected String pathToClientCert
	public CertificateConnectionFactory() {
	}
	
	
	@Override
	public Connection connectTo(Exchange exchange) {
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load
	}
}

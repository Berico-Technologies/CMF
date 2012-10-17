package cmf.eventing.berico;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.bus.berico.EnvelopeHelper;
import cmf.eventing.IInboundEventProcessor;
import cmf.eventing.IOutboundEventProcessor;
import cmf.security.ICertificateProvider;

public class DigitalSignatureProcessor implements IInboundEventProcessor, IOutboundEventProcessor {

	protected ICertificateProvider certProvider;
	protected KeyPair cert;
	protected Logger log;
	
	
	public DigitalSignatureProcessor(ICertificateProvider certProvider) {
		this.log = LoggerFactory.getLogger(this.getClass());
		
		try {
			this.cert = certProvider.getCertificate();
		}
		catch(Exception ex) {}
	}
	
	
	@Override
	public void processOutbound(Object event, Envelope envelope,
			Map<String, Object> context) {
		
		try {
			EnvelopeHelper env = new EnvelopeHelper(envelope);
			
			Signature instance = Signature.getInstance("SHA1withRSA");
			instance.initSign(this.cert.getPrivate());
			instance.update(envelope.getPayload());			
			
			env.setDigitalSignature(instance.sign());
			env.setSenderIdentity()
		}
		catch(Exception ex) {
			
		}
	}

	@Override
	public boolean processInbound(Object event, Envelope envelope,
			Map<String, Object> context) {

		try {
			EnvelopeHelper env = new EnvelopeHelper(envelope);
			String senderIdentity = env.getSenderIdentity();
			
			X509Certificate senderCert = this.certProvider.getCertificateFor(senderIdentity);
			
			Signature instance = Signature.getInstance("SHA1withRSA");
			instance.initVerify(senderCert);
			instance.update(envelope.getPayload());
			
			instance.verify(env.getDigitalSignature());
		}
		catch(Exception ex) {
			
		}
	}

}

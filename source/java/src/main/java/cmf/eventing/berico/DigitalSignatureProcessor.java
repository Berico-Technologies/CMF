package cmf.eventing.berico;

import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.Envelope;
import cmf.bus.berico.EnvelopeHelper;
import cmf.eventing.IInboundEventProcessor;
import cmf.eventing.IOutboundEventProcessor;
import cmf.security.CredentialHolder;
import cmf.security.ICertificateProvider;

public class DigitalSignatureProcessor implements IInboundEventProcessor, IOutboundEventProcessor {

	protected ICertificateProvider certProvider;
	protected CredentialHolder credentials;
	protected Logger log;
	
	
	public DigitalSignatureProcessor(ICertificateProvider certProvider) {
		this.log = LoggerFactory.getLogger(this.getClass());
		
		try {
			this.credentials = certProvider.getCredentials();
		}
		catch(Exception ex) {}
	}
	
	
	@Override
	public void processOutbound(Object event, Envelope envelope,
			Map<String, Object> context) throws Exception {
		
		try {
			EnvelopeHelper env = new EnvelopeHelper(envelope);
			
			Signature instance = Signature.getInstance("SHA1withRSA");
			instance.initSign(this.credentials.getPrivateKey());
			instance.update(envelope.getPayload());			
			
			env.setDigitalSignature(instance.sign());
			env.setSenderIdentity(this.credentials.getCertificate().getSubjectX500Principal().getName());
		}
		catch(Exception ex) {
			log.error("Exception while signing outbound event", ex);
			throw ex;
		}
	}

	@Override
	public boolean processInbound(Object event, Envelope envelope,
			Map<String, Object> context) throws Exception {

		try {
			EnvelopeHelper env = new EnvelopeHelper(envelope);
			String senderIdentity = env.getSenderIdentity();
			
			X509Certificate senderCert = this.certProvider.getCertificateFor(senderIdentity);
			
			Signature instance = Signature.getInstance("SHA1withRSA");
			instance.initVerify(senderCert);
			instance.update(envelope.getPayload());
			
			return instance.verify(env.getDigitalSignature());
		}
		catch(Exception ex) {
			return false;
		}
	}
}

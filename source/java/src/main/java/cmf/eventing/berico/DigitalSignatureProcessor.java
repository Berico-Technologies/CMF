package cmf.eventing.berico;

import java.security.Signature;
import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cmf.bus.berico.EnvelopeHelper;
import cmf.security.CredentialHolder;
import cmf.security.ICertificateProvider;

public class DigitalSignatureProcessor implements IInboundEventProcessor, IOutboundEventProcessor {

    protected ICertificateProvider certProvider;
    protected CredentialHolder credentials;
    protected Logger log;

    public DigitalSignatureProcessor(ICertificateProvider certProvider) throws Exception {
        this.certProvider = certProvider;
        log = LoggerFactory.getLogger(this.getClass());

        try {
            credentials = certProvider.getCredentials();
        } catch (Exception ex) {
            log.error("Failed to get client's PKI credentials", ex);
            throw ex;
        }
    }

    @Override
    public boolean processInbound(ProcessingContext context) throws Exception {

        boolean verified = false;

        try {
            EnvelopeHelper env = new EnvelopeHelper(context.getEnvelope());
            String senderIdentity = env.getSenderIdentity();

            // remove spaces from the sender identity
            senderIdentity = senderIdentity.replace(", ", ",");

            X509Certificate senderCert = certProvider.getCertificateFor(senderIdentity);

            Signature instance = Signature.getInstance("SHA1withRSA");
            instance.initVerify(senderCert);
            instance.update(env.getPayload());

            verified = instance.verify(env.getDigitalSignature());

            if (false == verified) {
                log.warn("Event may have been tampered with (id:{})", env.getMessageId().toString());
            }
        } catch (Exception ex) {
            log.error("Failed to verify sender's digital signature", ex);
            throw ex;
        }

        return verified;
    }

    @Override
    public void processOutbound(ProcessingContext context) throws Exception {

        try {
            EnvelopeHelper env = new EnvelopeHelper(context.getEnvelope());

            Signature instance = Signature.getInstance("SHA1withRSA");
            instance.initSign(credentials.getPrivateKey());
            instance.update(env.getPayload());

            env.setDigitalSignature(instance.sign());

            // make sure that the sender identity has spaces between LDAP elements
            String sender = credentials.getCertificate().getSubjectX500Principal().getName();
            sender = sender.replaceAll(",", ", ");

            env.setSenderIdentity(sender);
        } catch (Exception ex) {
            log.error("Exception while signing outbound event", ex);
            throw ex;
        }
    }
}

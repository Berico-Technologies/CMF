package cmf.bus.pubsub.processor;

import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.processor.IInboundProcessor;
import cmf.bus.core.processor.IOutboundProcessor;

public class SigningProcessor implements IInboundProcessor, IOutboundProcessor {

    public static final String SIGNATURE_HEADER = "cmf.bus.signature";

    private ISigningProvider signingProvider;

    public SigningProcessor() {

    }

    @Override
    public void processInbound(IEnvelope envelope, Map<String, Object> context) {
        // TODO: how to get public key? maybe this is a requirement for context
        String publicKey = "public key";
        String signature = envelope.getHeader(SIGNATURE_HEADER);
        byte[] clearText = envelope.getPayload();
        byte[] cypherText = StringUtils.getBytesUtf8(signature);
        if (!signingProvider.signatureIsValid(publicKey, clearText, cypherText)) {
            throw new InvalidSignatureException("Incoming envelope has an invalid signature");
        }
    }

    @Override
    public void processOutbound(IEnvelope envelope, Map<String, Object> context) {
        // TODO: how to get private key? maybe this is a requirement for context
        String privateKey = "private key";
        byte[] clearText = envelope.getPayload();
        byte[] cypherText = signingProvider.getSignature(privateKey, clearText);
        String signature = StringUtils.newStringUtf8(cypherText);
        envelope.setHeader(SIGNATURE_HEADER, signature);
    }

}

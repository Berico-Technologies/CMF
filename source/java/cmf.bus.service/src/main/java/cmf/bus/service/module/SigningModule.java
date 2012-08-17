package cmf.bus.service.module;

import org.apache.commons.codec.binary.StringUtils;

import cmf.bus.core.ISigningProvider;
import cmf.bus.core.IEnvelope;
import cmf.bus.core.IReceiveModule;
import cmf.bus.core.ISendModule;

public class SigningModule implements IReceiveModule, ISendModule {

    public static final String SIGNATURE_HEADER = "cmf.bus.signature";

    private ISigningProvider signingProvider;

    public SigningModule() {

    }

    @Override
    public void receive(IEnvelope envelope) {
        // TODO: how to get public key? maybe this is a requirement for context
        String publicKey = "public key";
        String signature = envelope.getHeader(SIGNATURE_HEADER);
        byte[] clearText = envelope.getPayload();
        byte[] cypherText = StringUtils.getBytesUtf8(signature);
        if (!signingProvider.signatureIsValid(publicKey, clearText, cypherText)) {
            throw new InvalidSignatureException("Incoming envelope has an invalid signature.");
        }
    }

    @Override
    public void send(IEnvelope envelope) {
        // TODO: how to get private key? maybe this is a requirement for context
        String privateKey = "private key";
        byte[] clearText = envelope.getPayload();
        byte[] cypherText = signingProvider.getSignature(privateKey, clearText);
        String signature = StringUtils.newStringUtf8(cypherText);
        envelope.setHeader(SIGNATURE_HEADER, signature);
    }

}

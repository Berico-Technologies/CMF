package cmf.bus.core.processor;

import java.util.Map;

import cmf.bus.core.IEnvelope;

public class EncryptionEnvelopeProcessor implements IInboundEnvelopeProcessor, IOutboundEnvelopeProcessor {

    private IEncryptionProvider encryptionProvider;

    public EncryptionEnvelopeProcessor() {

    }

    @Override
    public void processInbound(IEnvelope envelope, Map<String, Object> context) {
        // TODO: how to get private key? maybe this is a requirement for context
        String privateKey = "private key";
        byte[] cypherText = envelope.getPayload();
        byte[] clearText = encryptionProvider.decrypt(privateKey, cypherText);
        envelope.setPayload(clearText);
    }

    @Override
    public void processOutbound(IEnvelope envelope, Map<String, Object> context) {
        // TODO: how to get public key? maybe this is a requirement for context
        String publicKey = "public key";
        byte[] clearText = envelope.getPayload();
        byte[] cypherText = encryptionProvider.encrypt(publicKey, clearText);
        envelope.setPayload(cypherText);
    }

    public void setEncryptionProvider(IEncryptionProvider encryptionProvider) {
        this.encryptionProvider = encryptionProvider;
    }

}

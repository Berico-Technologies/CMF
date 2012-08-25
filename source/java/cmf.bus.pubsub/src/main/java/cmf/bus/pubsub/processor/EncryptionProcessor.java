package cmf.bus.pubsub.processor;

import java.util.Map;

import cmf.bus.core.IEnvelope;
import cmf.bus.core.processor.IInboundProcessor;
import cmf.bus.core.processor.IOutboundProcessor;

public class EncryptionProcessor implements IInboundProcessor, IOutboundProcessor {

    private IEncryptionProvider encryptionProvider;

    public EncryptionProcessor() {

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

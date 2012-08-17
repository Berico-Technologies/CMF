package cmf.bus.service.module;

import cmf.bus.core.IEncryptionProvider;
import cmf.bus.core.IEnvelope;
import cmf.bus.core.IReceiveModule;
import cmf.bus.core.ISendModule;

public class EncryptionModule implements IReceiveModule, ISendModule {

    private IEncryptionProvider encryptionProvider;

    public EncryptionModule() {

    }

    public void setEncryptionProvider(IEncryptionProvider encryptionProvider) {
        this.encryptionProvider = encryptionProvider;
    }

    @Override
    public void receive(IEnvelope envelope) {
        // TODO: how to get private key? maybe this is a requirement for context
        String privateKey = "private key";
        byte[] cypherText = envelope.getPayload();
        byte[] clearText = encryptionProvider.decrypt(privateKey, cypherText);
        envelope.setPayload(clearText);
    }

    @Override
    public void send(IEnvelope envelope) {
        // TODO: how to get public key? maybe this is a requirement for context
        String publicKey = "public key";
        byte[] clearText = envelope.getPayload();
        byte[] cypherText = encryptionProvider.encrypt(publicKey, clearText);
        envelope.setPayload(cypherText);
    }

}

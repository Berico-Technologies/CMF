package cmf.bus.pubsub.processor;

public interface IEncryptionProvider {

    byte[] decrypt(String privateKey, byte[] cypherText);

    byte[] encrypt(String publicKey, byte[] clearText);

}

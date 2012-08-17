package cmf.bus.core;

public interface IEncryptionProvider {

    byte[] encrypt(String publicKey, byte[] clearText);

    byte[] decrypt(String privateKey, byte[] cypherText);

}

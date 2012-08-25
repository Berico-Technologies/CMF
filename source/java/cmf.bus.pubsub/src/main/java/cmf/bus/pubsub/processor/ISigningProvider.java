package cmf.bus.pubsub.processor;

public interface ISigningProvider {

    byte[] getSignature(String privateKey, byte[] clearText);

    boolean signatureIsValid(String publicKey, byte[] clearText, byte[] cypherText);

}

package cmf.bus.service.module;

@SuppressWarnings("serial")
public class InvalidSignatureException extends RuntimeException {

    public InvalidSignatureException() {
        super();
    }

    public InvalidSignatureException(String message) {
        super(message);
    }

}
